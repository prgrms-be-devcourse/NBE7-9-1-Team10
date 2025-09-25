package com.example.cafe.domain.order.Dto;

import com.example.cafe.domain.order.Entity.Orders;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FindAllOrderDto {

    private final Long orderId;
    private final String email;
    private final String address;
    private final LocalDateTime orderDate;
    private final int totalPrice;
    private final List<OrderItemResponseDto> items;

    public FindAllOrderDto(Orders order){
        this.orderId = order.getId();
        this.email = order.getCustomerEmail();
        this.address = order.getAddress();
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.totalPrice();
        this.items = order.
                getOrderItems().
                stream().
                map(m -> new OrderItemResponseDto(m.getItem(), m.getQty()))
                .collect(Collectors.toList());
    }
}
