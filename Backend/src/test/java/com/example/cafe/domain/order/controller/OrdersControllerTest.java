package com.example.cafe.domain.order.controller;

import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Controller.OrderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class OrdersControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderController orderController;

    @Test
    @DisplayName("주문 생성 API")
    void t1() throws Exception {
        // 테스트용 아이템 2개 넣기
        // itemRepository를 쓰면 단위테스트
        Item item1 = itemRepository.save(new Item("아메리카노", 3000, "imageUrl1"));
        Item item2 = itemRepository.save(new Item("라떼", 4500, "imageUrl2"));

        // 요청 바디 구성
        Map<String, Object> req = new HashMap<>();
        req.put("email", "test@example.com");
        req.put("address", "서울시 강남구");
        req.put("items", List.of(
                Map.of("id", item1.getItemId(), "qty", 2),
                Map.of("id", item2.getItemId(), "qty", 1)
        ));

        String reqJson = objectMapper.writeValueAsString(req);

        // when 이렇게 요청을 받는다면
        ResultActions result = mvc.perform(
                post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson)
        ).andDo(print());

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



}


