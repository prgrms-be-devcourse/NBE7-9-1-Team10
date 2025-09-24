package com.example.cafe.domain.order.Entity;

import com.example.cafe.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "orderitemId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    private int qty;

    public OrderItem(Item item, int qty){
        this.item = item;
        this.qty = qty;
    }
}
