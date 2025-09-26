package com.example.cafe.domain.order.Dto;

import com.example.cafe.domain.order.Entity.Orders;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FindAllOrderResponse {

    private final List<FindAllOrderDto> orders;

    public FindAllOrderResponse(List<Orders> orders) {
        this.orders = orders
                .stream()
                .map(m -> new FindAllOrderDto(m))
                .collect(Collectors.toList());
    }
}
