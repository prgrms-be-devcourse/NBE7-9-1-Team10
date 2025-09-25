package com.example.cafe.domain.order.Controller;

import com.example.cafe.domain.order.Dto.FindAllOrderByEmailResponse;
import com.example.cafe.domain.order.Dto.FindAllOrderResponse;
import com.example.cafe.domain.order.Dto.OrderCreateRequest;
import com.example.cafe.domain.order.Dto.OrderCreateResponse;
import com.example.cafe.domain.order.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "주문 관리", description = "주문 관련 API")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다")
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

    @GetMapping("/api/v1/orders/user")
    public ResponseEntity<FindAllOrderByEmailResponse> FindAllOrderByEmailResponse(@RequestParam String email){
        if(email != null){
            FindAllOrderByEmailResponse findAllOrderByEmailResponse = orderService.findAllOrderByEmailResponse(email);
            return ResponseEntity.ok(findAllOrderByEmailResponse);
        }
        return ResponseEntity.badRequest().build();
    }
}
