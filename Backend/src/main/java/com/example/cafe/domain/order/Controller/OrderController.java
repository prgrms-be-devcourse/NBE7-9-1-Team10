package com.example.cafe.domain.order.Controller;

import com.example.cafe.domain.order.Dto.OrderCreateRequest;
import com.example.cafe.domain.order.Dto.OrderCreateResponse;
import com.example.cafe.domain.order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders")
    public OrderCreateResponse orderCreateResponse(@RequestBody OrderCreateRequest request){

        return orderService.createOrder(request);

    }

//    @GetMapping("/api/v1/adm/orders")
//    public


}
