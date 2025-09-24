package com.example.cafe.domain.item.dto;

import com.example.cafe.domain.item.entity.Item;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ItemResponse {

    private final Long itemId;
    private final String itemName;
    private final Integer price;
    private final LocalDate createdDate;
    private final String status;
    private final String imageUrl;

    public ItemResponse(Item item) {
        this.itemId = item.getItemId();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.createdDate = item.getCreatedDate();
        this.status = item.getStatus();
        this.imageUrl = item.getImageUrl();
    }

    public static ItemResponse from(Item item) {
        return new ItemResponse(item);
    }
}
