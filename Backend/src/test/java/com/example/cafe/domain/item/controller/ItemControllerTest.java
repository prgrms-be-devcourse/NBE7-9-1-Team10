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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemToJson(itemName, price, imageUrl))
        ).andDo(print());
    }

    //------------------------- TEST ------------------------------
    @Test
    @DisplayName("상품 생성 API")
    void t1() throws Exception {
        var result = postItem("아메리카노", 3000, "testURL");

        result
                .andExpect(handler().handlerType(ItemController.class))
                .andExpect(handler().methodName("createItem"))
                .andExpect(status().isCreated());
    }

}
