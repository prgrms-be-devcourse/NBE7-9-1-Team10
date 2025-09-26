package com.example.cafe.integration.scenario;

import com.fasterxml.jackson.core.type.TypeReference;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class IntegrationScenarioTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    //------------------------ HELPER ------------------------

    private long createItem(String name, int price) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("itemName", name);
        req.put("price", price);

        ResultActions res = mvc.perform(
                        post("/api/v1/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemId").exists());

        Map<String, Object> body = readMap(res);
        return ((Number) body.get("itemId")).longValue();
    }

    private long createOrder(String email, String address, List<Map<String, Object>> items) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", email);
        req.put("address", address);
        req.put("items", items);

        ResultActions res = mvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(email));

        Map<String, Object> body = readMap(res);
        return ((Number) body.get("id")).longValue();
    }
    private Map<String, Object> readMap(ResultActions res) throws Exception {
        String content = res.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }

    private Map<String, Object> getItem(long itemId) throws Exception {
        ResultActions res = mvc.perform(
                        get("/api/v1/items/{itemId}", itemId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
        return readMap(res);
    }

    //------------------------ TEST ------------------------
    @Test
    @DisplayName("mvp 시나리오: 상품추가(4) -> 주문 -> 상품수정 -> 상품제거 -> 이메일 단건주문조회")
    void mvpScenario() throws Exception {
        // 상품 4개 생성
        long itemId1 = createItem("아메리카노", 2000);
        long itemId2 = createItem("카페라떼", 3500);
        long itemId3 = createItem("카푸치노", 3300);
        long itemId4 = createItem("레몬에이드", 4000);

        // 주문 생성
        String email = "test@example.com";
        String address = "서울 광진구";
        long orderId = createOrder(email, address, List.of(
                Map.of("id", itemId1, "qty", 2),
                Map.of("id", itemId2, "qty", 1),
                Map.of("id", itemId3, "qty", 3),
                Map.of("id", itemId4, "qty", 4)
        ));

        // 상품 수정 2번 상품의 정보 수정
        Map<String, Object> putBody = new HashMap<>();
        putBody.put("itemName", "바닐라라떼");
        putBody.put("price", 3800);

        mvc.perform(
                        put("/api/v1/items/{itemId}", itemId2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(putBody))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // 상품 제거 3번 상품을 제거
        mvc.perform(delete("/api/v1/items/{itemId}", itemId3))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 이메일로 주문 조회
        ResultActions getOrderRes = mvc.perform(
                        get("/api/v1/orders/user").param("email", email)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.orders").isArray());

        // 여기서 테스트 진행
        // 2번 상품의 수정 여부 확인
        // 3번 상품이 제거 여부 확인
        // totalprice값이 원래 값과 같은지 확인하기
        Map<String, Object> orderByEmail = readMap(getOrderRes);
        List<Map<String, Object>> orders = (List<Map<String, Object>>) orderByEmail.get("orders");
        //orders값이 들어왔는지 확인
        assertThat(orders).isNotEmpty();

        //가장 최근 주문(현재주문) 가져오기 -> id맞는지 확인하기
        Map<String, Object> currentOrder = orders.get(orders.size() - 1);
        assertThat(((Number) currentOrder.get("orderId")).longValue()).isEqualTo(orderId);

        //주문된 아이템 가져오기
        List<Map<String, Object>> orderItems = (List<Map<String, Object>>) currentOrder.get("items");

        //상품이 순서대로 들어간다생각하고 2번째, 3번째 아이디 가져오기
        long itemId2FromOrder = ((Number)(orderItems.get(1).get("id"))).longValue();
        long itemId3FromOrder = ((Number)(orderItems.get(2).get("id"))).longValue();

        //2번 아이템 수정 여부 확인 -> db에서 변경된 값 확인
        Map<String, Object> currentItem2 = getItem(itemId2FromOrder);
        assertThat(((Number) currentItem2.get("price")).longValue()).isEqualTo(3800);
        assertThat(((String) currentItem2.get("itemName"))).isEqualTo("바닐라라떼");

        //3번 아이템 삭제 여부 확인 -> db에서 변경된 값 확인
        Map<String, Object> currentItem3 = getItem(itemId3FromOrder);
        assertThat(currentItem3).isNotNull();
        assertThat(((String) currentItem3.get("status"))).isEqualTo("noQty");

        //총 가격이 원래 주문한 가격과 맞는지 확인
        assertThat(((Number) currentOrder.get("totalPrice")).intValue()).isEqualTo((2000*2)+(3500*1)+(3300*3)+(4000*4));

    }
}
