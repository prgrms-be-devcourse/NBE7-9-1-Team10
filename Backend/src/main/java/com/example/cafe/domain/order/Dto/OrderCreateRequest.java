package com.example.cafe.domain.order.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {

    private final String email;
    private final String address;
    private final List<OrderItemRequestDto> items;

    public OrderCreateRequest(String email, String address, List<OrderItemRequestDto> items) {
        this.email = email;
        this.address = address;
        this.items = items;
    }
}
