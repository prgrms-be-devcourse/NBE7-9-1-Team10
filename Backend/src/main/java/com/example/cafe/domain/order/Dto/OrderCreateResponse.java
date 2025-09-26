package com.example.cafe.domain.order.Dto;

import com.example.cafe.domain.order.Entity.Orders;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderCreateResponse {

    private final Long id;
    private final String email;
    private final LocalDateTime orderDate;
    private final int totalPrice;
    private final int deliveryStatus;
    private final List<OrderItemResponseDto> items;

    public OrderCreateResponse(Orders order){
        this.id = order.getId();
        this.email = order.getCustomerEmail();
        this.orderDate = order.getOrderDate();
        this.deliveryStatus = order.calculateCurrentDeliveryStatus();
        this.totalPrice = order.getTotalPrice();
        this.items = order.
                getOrderItems().
                stream().
                map(m -> new OrderItemResponseDto(m.getItem(), m.getQty()))
                .collect(Collectors.toList());
    }
}
