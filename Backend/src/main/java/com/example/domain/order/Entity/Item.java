package com.example.domain.order.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item { //임시 테이블

    @Id @GeneratedValue
    @Column(name = "itemId")
    private Long id;

    private String itemName;

    private int price;

    private LocalDateTime createdDate;

    private String imageUrl;

    public Item(String itemName, int price, String imageUrl) {
        this.itemName = itemName;
        this.price = price;
        this.imageUrl = imageUrl;
    }


}
