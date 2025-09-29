package com.example.cafe.domain.order.Dto;
import lombok.Getter;

import java.util.List;


@Getter
public class OrderSalesResponse {
    private final Long totalSale;
    private final List<ItemSalesDto> itemSales;


    public OrderSalesResponse(Long totalSale, List<ItemSalesDto> itemSales) {
        this.totalSale = totalSale;
        this.itemSales = itemSales;
    }
}


