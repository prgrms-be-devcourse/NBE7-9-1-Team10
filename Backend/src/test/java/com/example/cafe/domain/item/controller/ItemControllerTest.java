package com.example.cafe.domain.item.controller;

import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ItemControllerTest {

    @Autowired private MockMvc         mvc;
    @Autowired private ObjectMapper    objectMapper;
    @Autowired private ItemRepository  itemRepository;
    @Autowired private OrderRepository orderRepository;

    //------------------------- HELPER ------------------------------

    private String itemToJson(String itemName, Integer price, String imageUrl) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("itemName", itemName);
        req.put("price", price);
        req.put("imageUrl", imageUrl);
        return objectMapper.writeValueAsString(req);
    }

    private ResultActions postItem(String itemName, Integer price, String imageUrl) throws Exception {
        return mvc.perform(
                post("/api/v1/items")
                        .header("User-Email", "admin@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemToJson(itemName, price, imageUrl))
        ).andDo(print());
    }

    //이메일 입력으로 admin 아닌 케이스 검증
    private ResultActions postItem(String itemName, Integer price, String imageUrl, String userEmail) throws Exception {
        return mvc.perform(
                post("/api/v1/items")
                        .header("User-Email", userEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemToJson(itemName, price, imageUrl))
        ).andDo(print());
    }

    private long createItemAndGetId(String itemName, Integer price, String imageUrl) throws Exception {
        MvcResult r = postItem(itemName, price, imageUrl).andReturn();
        return objectMapper.readTree(r.getResponse().getContentAsString()).get("itemId").asLong();
    }

    //------------------------- TEST ------------------------------
    @Test
    @DisplayName("상품 생성 API")
    void t1() throws Exception {
        var result = postItem("아메리카노", 3000, "testURL");

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("createItem"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("아메리카노"))
                .andExpect(jsonPath("$.price").value(3000))
                .andExpect(jsonPath("$.imageUrl").value("testURL"));
    }

    @Test
    @DisplayName("상품 생성 API - 관리자가 아닐경우 실패")
    void t2() throws Exception {
        var result = postItem("아메리카노", 3000, "testURL", "user1@test.com");

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("createItem"))
                .andExpect(status().isForbidden()) //403에러
                .andExpect(jsonPath("$.error").value("관리자가 아닙니다. 권한이 필요합니다."));
    }

    @Test
    @DisplayName("전체 제품 조회 API")
    void t3() throws Exception {
        long id1 = createItemAndGetId("아메리카노", 3000, "testURL");
        long id2 = createItemAndGetId("카페라떼", 4500, "testURL2");

        ResultActions result = mvc.perform(
                get("/api/v1/items")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("getAllItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].itemId").value(hasItems((int) id1, (int) id2)))
                .andExpect(jsonPath("$[*].itemName").value(hasItems("아메리카노", "카페라떼")));
    }

    @Test
    @DisplayName("단일 제품 조회 API")
    void t4() throws Exception {
        long id1 = createItemAndGetId("아메리카노", 3000, "testURL");

        ResultActions result = mvc.perform(
                get("/api/v1/items/{itemId}", id1)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value((int) id1))
                .andExpect(jsonPath("$.itemName").value("아메리카노"));
    }

    @Test
    @DisplayName("제품 수정 API")
    void t5() throws Exception {
        long id1 = createItemAndGetId("아메리카노", 3000, "testURL");

        ResultActions result = mvc.perform(
                put("/api/v1/items/{itemId}", id1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("User-Email", "admin@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemToJson("카페라떼", 4500, "testURL2"))
        ).andDo(print());

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("updateItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("카페라떼"))
                .andExpect(jsonPath("$.price").value((int) 4500));
    }

    @Test
    @DisplayName("제품 삭제 API")
    void t6() throws Exception {
        long id1 = createItemAndGetId("아메리카노", 3000, "testURL");

        ResultActions result = mvc.perform(
                delete("/api/v1/items/{itemId}", id1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("User-Email", "admin@email.com")
        ).andDo(print());

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("deleteItem"))
                .andExpect(status().isNoContent());
    }
}
