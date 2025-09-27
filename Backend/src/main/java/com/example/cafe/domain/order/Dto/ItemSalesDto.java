package com.example.cafe.domain.order.Dto;

import lombok.Getter;

@Getter
public class ItemSalesDto {
    private final Long id;
    private final String itemName;
    private final Long totalQty;

    public ItemSalesDto(Long id, String itemName, Long totalQty)
    {
        this.id = id;
        this.itemName = itemName;
        this.totalQty = totalQty;
    }

}