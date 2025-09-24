package com.example.cafe.domain.item.controller;

import com.example.cafe.domain.item.dto.ItemResponse;
import com.example.cafe.domain.item.dto.ItemUpdateRequest;
import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    //매 테스트 직전 호출된다. MockMvc환경 세팅
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        //itemCOntroller 하나만 등록해서 테스트 -> 빠르게 작동한다.
        mvc = MockMvcBuilders.standaloneSetup(itemController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private ItemResponse response(String name, int price, String url) {
        Item item = Item.builder()
                .itemName(name)
                .price(price)
                .imageUrl(url)
                .build(); // createdDate=now, status=onSale
        return ItemResponse.from(item);
    }

    @Test
    @DisplayName("상품 생성 API")
    void t1() throws Exception {

        Map<String, Object> req = new HashMap<>();
        req.put("itemName", "테스트 아이템");
        req.put("price", 5000);
        req.put("imageUrl", "http://image.png");

        String reqJson = objectMapper.writeValueAsString(req);

        //서비스 (가짜)목을 만들어서 response라고 대답하게 설정, mokito 정리할 것
        given(itemService.createItem(any()))
                .willReturn(response("테스트 아이템", 5000, "http://image.png"));

        mvc.perform(post("/api/v1/admin/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemName").value("테스트 아이템"))
                .andExpect(jsonPath("$.price").value(5000))
                .andExpect(jsonPath("$.imageUrl").value("http://image.png"))
                .andExpect(jsonPath("$.status").value("onSale"))
                .andExpect(jsonPath("$.createdDate").exists());
    }

    @Test
    @DisplayName("단건 조회 API")
    void t2() throws Exception {
        given(itemService.getItem(1L)).willReturn(response("아메리카노", 3000, "http://a.png"));

        mvc.perform(get("/api/v1/admin/items/{itemId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("아메리카노"))
                .andExpect(jsonPath("$.price").value(3000))
                .andExpect(jsonPath("$.imageUrl").value("http://a.png"))
                .andExpect(jsonPath("$.status").value("onSale"))
                .andExpect(jsonPath("$.createdDate").exists());

        verify(itemService).getItem(1L);
    }

    @Test
    @DisplayName("상품 목록 조회 API")
    void t3() throws Exception {
        ItemResponse r1 = response("아메리카노", 3000, "http://a.png");
        ItemResponse r2 = response("라떼", 4000, "http://b.png");
        given(itemService.getAllItems()).willReturn(List.of(r1, r2));

        mvc.perform(get("/api/v1/admin/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].itemName").value("아메리카노"))
                .andExpect(jsonPath("$[1].itemName").value("라떼"));

        verify(itemService).getAllItems();
    }

    @Test
    @DisplayName("상품 수정 API")
    void t4() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("itemName", "업데이트 아이템");
        req.put("price", 5500);
        req.put("imageUrl", "http://new.png");
        req.put("status", "onSale");

        String reqJson = objectMapper.writeValueAsString(req);

        given(itemService.updateItem(eq(1L), any(ItemUpdateRequest.class)))
                .willReturn(response("업데이트 아이템", 5500, "http://new.png"));

        mvc.perform(put("/api/v1/admin/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("업데이트 아이템"))
                .andExpect(jsonPath("$.price").value(5500))
                .andExpect(jsonPath("$.imageUrl").value("http://new.png"))
                .andExpect(jsonPath("$.status").value("onSale"))
                .andExpect(jsonPath("$.createdDate").exists())
                .andDo(print());

        verify(itemService).updateItem(eq(1L), any());
    }

    @Test
    @DisplayName("상품 삭제 API")
    void t5() throws Exception {
        //delete시 아무일도 없도록 stub 걸기, 예외발생 없이 그냥 통과
        doNothing().when(itemService).deleteItem(1L);

        mvc.perform(delete("/api/v1/admin/items/{itemId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(itemService).deleteItem(1L);
    }
}
