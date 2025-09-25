package com.example.cafe.domain.order.Controller;

import com.example.cafe.domain.order.Dto.OrderCreateRequest;
import com.example.cafe.domain.order.Dto.OrderCreateResponse;
import com.example.cafe.domain.order.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문 관리", description = "주문 관련 API")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다")
    @PostMapping("/api/v1/orders")
    public OrderCreateResponse orderCreateResponse(@RequestBody OrderCreateRequest request){
        return orderService.createOrder(request);
    }

//    @GetMapping("/api/v1/adm/orders")
//    public


}
