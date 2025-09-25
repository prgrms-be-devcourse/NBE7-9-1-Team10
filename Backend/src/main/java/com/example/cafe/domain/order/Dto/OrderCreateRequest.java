package com.example.cafe.domain.order.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {

    private final String email;
    private final int totalPrice;
    private final String address;
    private final List<OrderItemRequestDto> items;

    public OrderCreateRequest(String email, int totalPrice, String address, List<OrderItemRequestDto> items) {
        this.email = email;
        this.totalPrice = totalPrice;
        this.address = address;
        this.items = items;
    }
}
