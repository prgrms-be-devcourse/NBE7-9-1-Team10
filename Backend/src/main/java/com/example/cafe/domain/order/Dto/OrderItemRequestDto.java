package com.example.cafe.domain.order.Dto;

import lombok.Getter;

@Getter
public class OrderItemRequestDto {
    private final Long id;
    private final int qty;

    public OrderItemRequestDto(Long id, int qty){
        this.id = id;
        this.qty = qty;
    }
}
