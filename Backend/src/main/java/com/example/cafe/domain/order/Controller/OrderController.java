package com.example.cafe.domain.order.Controller;

import com.example.cafe.domain.order.Dto.FindAllOrderResponse;
import com.example.cafe.domain.order.Dto.OrderCreateRequest;
import com.example.cafe.domain.order.Dto.OrderCreateResponse;
import com.example.cafe.domain.order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders")
    public ResponseEntity<OrderCreateResponse> orderCreateResponse(@RequestBody OrderCreateRequest request){
        OrderCreateResponse orderCreateResponse = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreateResponse);
    }

    @GetMapping("/api/v1/orders")
    public ResponseEntity<FindAllOrderResponse> fIndAllOrderResponse(){
        FindAllOrderResponse findAllOrderResponse = orderService.findAllOrders();
        return ResponseEntity.ok(findAllOrderResponse);
    }
}
