package com.example.cafe.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1AdmPostControllerTest {

    @Autowired
    private MockMvc mvc;

    //일단 임포트 없이
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 생성 테스트")
    void t1() throws Exception {

        String orderDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> request = new HashMap<>();
        request.put("email", "email@example.com");
        request.put("orderDate", orderDate);
        request.put("totalPrice", 20000);
        request.put("address", "서울시 강남구");

        List<Map<String, Object>> items = List.of(
                Map.of("id", 1, "qty", 2),
                Map.of("id", 2, "qty", 1)
        );
        request.put("items", items);

        // when & then
        mvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk()) // 컨트롤러에서 반환하는 상태 코드에 맞게 수정
                .andExpect(jsonPath("$.email").value("email@example.com"))
                .andExpect(jsonPath("$.totalPrice").value(20000))
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[0].qty").value(2));
    }
}


