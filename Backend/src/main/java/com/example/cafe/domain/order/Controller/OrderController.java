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
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request){
        OrderCreateResponse orderCreateResponse = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreateResponse);
    }

    @Operation(summary = "전체 주문 조회", description = "모든 주문을 조회합니다")
    @GetMapping("/api/v1/orders")
    public ResponseEntity<FindAllOrderResponse> getAllOrders(){
        FindAllOrderResponse findAllOrderResponse = orderService.findAllOrders();
        return ResponseEntity.ok(findAllOrderResponse);
    }

    @Operation(summary = "배송준비 주문 조회", description = "배송준비 상태인 주문만 조회합니다")
    @GetMapping("/api/v1/orders/delivery-ready")
    public ResponseEntity<FindAllOrderResponse> getDeliveryReadyOrders(){
        FindAllOrderResponse findAllOrderResponse = orderService.findOrdersByDeliveryStatus(0);
        return ResponseEntity.ok(findAllOrderResponse);
    }

    @Operation(summary = "배송중 주문 조회", description = "배송중 상태인 주문만 조회합니다")
    @GetMapping("/api/v1/orders/delivery-in-progress")
    public ResponseEntity<FindAllOrderResponse> getDeliveryInProgressOrders(){
        FindAllOrderResponse findAllOrderResponse = orderService.findOrdersByDeliveryStatus(1);
        return ResponseEntity.ok(findAllOrderResponse);
    }

    @Operation(summary = "배송완료 주문 조회", description = "배송완료 상태인 주문만 조회합니다")
    @GetMapping("/api/v1/orders/delivery-completed")
    public ResponseEntity<FindAllOrderResponse> getDeliveryCompletedOrders(){
        FindAllOrderResponse findAllOrderResponse = orderService.findOrdersByDeliveryStatus(2);
        return ResponseEntity.ok(findAllOrderResponse);
    }

    @Operation(summary = "사용자별 주문 조회", description = "특정 이메일의 모든 주문을 조회합니다")
    @GetMapping("/api/v1/orders/user")
    public ResponseEntity<FindAllOrderByEmailResponse> findAllOrderByEmailResponse(@RequestParam String email){
        if(email != null){
            FindAllOrderByEmailResponse findAllOrderByEmailResponse = orderService.findAllOrderByEmailResponse(email);
            return ResponseEntity.ok(findAllOrderByEmailResponse);
        }
        return ResponseEntity.badRequest().build();
    }
}
