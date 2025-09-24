package com.example.cafe.domain.item.dto;

import com.example.cafe.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemUpdateRequest {

    private String itemName;
    private Integer price;

    private String imageUrl;

    public ItemUpdateRequest(String itemName, Integer price, String imageUrl) {
        this.itemName = itemName;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
