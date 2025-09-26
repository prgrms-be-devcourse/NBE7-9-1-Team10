package com.example.cafe.domain.order.controller;

import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Controller.OrderController;
import com.example.cafe.domain.order.Entity.Orders;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .andExpect(handler().methodName("createOrder"))
                .andExpect(status().isCreated())

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
        long id2 = createOrderAndGetId("testest@example.com", "부산 해운대");

        // when
        ResultActions result = mvc.perform(
                get("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getAllOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[*].orderId").value(hasItems((int) id1, (int) id2)))
                .andExpect(jsonPath("$.orders[*].email").value(hasItems("test@example.com", "testest@example.com")));
    }

    @Test
    @DisplayName("단건 조회(이메일) API")
    void t3() throws Exception {
        createOrderAndGetId("one@example.com", "서울 광진");

        // when
        ResultActions result = mvc.perform(
                get("/api/v1/orders/user")
                        .param("email", "one@example.com")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("findAllOrderByEmailResponse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("one@example.com"))
                .andExpect(jsonPath("$.orders").isArray());
    }

    @Test
    @DisplayName("NOT USED_일별 주문 조회 API")
    void t4() throws Exception {

        long todayOrder = createOrderAndGetId("day@example.com", "서울 강동");

        String today = LocalDate.now().toString();

        // when
        ResultActions result = mvc.perform(
                get("/api/v1/orders_daily")
                        .param("date", today)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getDailyOrders")) // 실제 메서드명으로 변경 필요
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[*].orderId").value(hasItem((int)todayOrder)))
                .andExpect(jsonPath("$.orders[*].email").value(hasItem("day@example.com")));
    }

    @Test
    @DisplayName("NOT USED_주문 취소 API")
    void t5() throws Exception {

        long id1 = createOrderAndGetId("delete@example.com", "서울 강동구");
        long id2 = createOrderAndGetId("delete2@example.com", "서울 강동구");

        // 삭제 전에 목록에 주문 두개 들어있는지 확인
        mvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*].orderId").value(hasItems((int) id1, (int) id2)))
                .andDo(print());

        // 주문 1 삭제
        ResultActions result = mvc.perform(delete("/api/v1/orders/{orderId}", id1))
                .andDo(print());

        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("cancelOrder")) // 실제 메서드명으로 변경 필요
                .andExpect(status().isNoContent());

        // 삭제 후 목록에서 id1이 더 이상 보이지 않아야 함
        mvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*].orderId").value(not(hasItem((int) id1))))
                .andDo(print());
    }

    @Test
    @DisplayName("배송 준비 상태 확인 API")
    void t6() throws Exception {
        long id1 = createOrderAndGetId("test@example.com", "서울 광진");
        Orders order = orderRepository.findById(id1).get();
        LocalDateTime now = LocalDateTime.now();

        // 현재가 14시 이후면 내일로 밀기 -> 무조건 배송준비상태
        order.updateOrderDate((now.getHour() >= 14 ? now.plusDays(1) : now));
        orderRepository.flush();

        ResultActions result = mvc.perform(
                get("/api/v1/orders/delivery-ready")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getDeliveryReadyOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[*].orderId").value(hasItem((int)id1)))
                .andDo(print());
    }

    @Test
    @DisplayName("배송 중 상태 확인 API")
    void t7() throws Exception {
        long id1 = createOrderAndGetId("test@example.com", "서울 광진");
        Orders order = orderRepository.findById(id1).get();
        LocalDateTime now = LocalDateTime.now();

        // 현재가 14시 전이면 어제로 보내기
        order.updateOrderDate((now.getHour() < 14 ? now : now.minusDays(1)));
        orderRepository.flush();

        ResultActions result = mvc.perform(
                get("/api/v1/orders/delivery-in-progress")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getDeliveryInProgressOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[*].orderId").value(hasItem((int)id1)))
                .andDo(print());
    }

    @Test
    @DisplayName("배송 완료 상태 확인 API")
    void t8() throws Exception {
        long id1 = createOrderAndGetId("test@example.com", "서울 광진");
        Orders order = orderRepository.findById(id1).get();
        LocalDateTime now = LocalDateTime.now();

        //2시 이후면 하루전, 아니면 이틀전
        order.updateOrderDate((now.getHour() >= 14 ? now.minusDays(2) : now.minusDays(1)));
        orderRepository.flush();

        ResultActions result = mvc.perform(
                get("/api/v1/orders/delivery-completed")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        result
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getDeliveryCompletedOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[*].orderId").value(hasItem((int)id1)))
                .andDo(print());
    }
}


