package com.example.cafe.domain.order.Dto;

import com.example.cafe.domain.item.entity.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemResponseDto {

    private Long id;
    private String name;
    private int qty;

    public OrderItemResponseDto(Item item, int qty) {
        this.id = item.getItemId();
        this.name = item.getItemName();
        this.qty = qty;
    }
}
