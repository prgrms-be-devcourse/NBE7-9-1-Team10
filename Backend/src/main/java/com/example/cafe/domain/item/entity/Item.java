package com.example.cafe.domain.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long itemId;

    @Column(name = "itemName", length = 100, nullable = false)
    private String itemName;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "createdDate")
    private LocalDate createdDate;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Builder
    public Item(String itemName, Integer price, String imageUrl) {
        this.itemName = itemName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdDate = LocalDate.now();
        this.status = "onSale";
    }

    // Setter 메소드들
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
