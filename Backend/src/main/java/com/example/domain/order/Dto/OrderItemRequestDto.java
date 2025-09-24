package com.example.domain.order.Dto;

import lombok.Getter;

@Getter
public class OrderItemRequestDto{
    private final Long itemId;
    private final int qty;

    public OrderItemRequestDto(Long itemId, int qty){
        this.itemId = itemId;
        this.qty = qty;
    }
}