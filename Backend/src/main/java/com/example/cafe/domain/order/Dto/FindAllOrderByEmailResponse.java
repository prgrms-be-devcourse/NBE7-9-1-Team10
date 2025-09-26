package com.example.cafe.domain.order.Dto;

import com.example.cafe.domain.order.Entity.Orders;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FindAllOrderByEmailResponse {
    private String email;
    private List<FindAllOrderByEmailDto> orders;

    public FindAllOrderByEmailResponse(String email, List<Orders> orders) {
        this.email = email;
        this.orders = orders
                .stream()
                .map(m -> new FindAllOrderByEmailDto(m))
                .collect(Collectors.toList());
    }

    @Getter
    static class FindAllOrderByEmailDto {
        private final Long orderId;
        private final String address;
        private final LocalDateTime orderDate;
        private final int totalPrice;
        private final int deliveryStatus;
        private final List<OrderItemResponseDto> items;

        public FindAllOrderByEmailDto(Orders order){
            this.orderId = order.getId();
            this.address = order.getAddress();
            this.orderDate = order.getOrderDate();
            this.totalPrice = order.totalPrice();
            this.deliveryStatus = order.calculateCurrentDeliveryStatus();
            this.items = order.
                    getOrderItems().
                    stream().
                    map(m -> new OrderItemResponseDto(m.getItem(), m.getQty()))
                    .collect(Collectors.toList());
        }
    }
}
