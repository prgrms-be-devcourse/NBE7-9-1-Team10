package com.example.cafe.domain.order.controller;

import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Controller.OrderController;
import com.example.cafe.domain.order.Repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class OrdersControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ItemRepository itemRepository;
    @Autowired private OrderRepository orderRepository;

    //------------------------ 픽스처 ------------------------
    // 공통 픽스처(Fixture 테스트 실행 전에 준비해둔 고정된 데이터/상태)
    private Long item1Id;
    private Long item2Id;

    //각 테스트 실행전 픽스처 설정
    @BeforeEach
    void setUp() {
        item1Id = itemRepository.save(new Item("아메리카노", 3000, "img1")).getItemId();
        item2Id = itemRepository.save(new Item("라떼", 4500, "img2")).getItemId();
    }

    //------------------------ 헬퍼 ------------------------
    //주문을 하나 만들고 아이디를 가져온다.
    private String orderJson(String email, String address, Map<Long, Integer> idQty) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", email);
        req.put("address", address);
        List<Map<String, Object>> items = idQty.entrySet().stream()
                .map(e -> Map.<String, Object>of("id", e.getKey(), "qty", e.getValue()))
                .collect(Collectors.toList());
        req.put("items", items);
        return objectMapper.writeValueAsString(req);
    }

    //주문 실행
    private ResultActions postOrder(String email, String address, Map<Long, Integer> idQty) throws Exception {
        return mvc.perform(
                post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(email, address, idQty))
        ).andDo(print());
    }

    //주문 생성하고 아이디만 가져오기
    private long createOrderAndGetId(String email, String address) throws Exception {
        MvcResult r = postOrder(email, address, Map.of(item1Id, 2, item2Id, 1)).andReturn();
        return objectMapper.readTree(r.getResponse().getContentAsString()).get("id").asLong();
    }

    //------------------------ 테스트 ------------------------
    @Test
    @DisplayName("주문 생성 API")
    void t1() throws Exception {
        // when 이렇게 요청을 받는다면
        var result = postOrder("test@example.com", "서울 강남", Map.of(item1Id, 2, item2Id, 1));

        // then 결과를 이렇게 예상한다
        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("orderCreateResponse"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.orderDate").exists())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.totalPrice").value(3000*2 + 4500*1));
    }

    @Test
    @DisplayName("전체 주문 조회 API")
    void t2() throws Exception {
        int beforeSize = orderRepository.findAll().size();
        long id1 = createOrderAndGetId("test@example.com", "서울 송파");
        long id2 = createOrderAndGetId("test@example.com", "서울 송파");
        long id3 = createOrderAndGetId("testest@example.com", "부산 해운대");

        // when
        ResultActions result = mvc.perform(
                get("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getOrders")) // 임시
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(beforeSize + 3));
    }


}


