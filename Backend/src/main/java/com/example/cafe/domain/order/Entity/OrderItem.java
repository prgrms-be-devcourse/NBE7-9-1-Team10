package com.example.cafe.domain.order.Entity;

import com.example.cafe.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "orderitemId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    private int qty;

    public static OrderItem createOrderItem(Item item, int qty){ //cascade 상태라 order 굳이 저장 안해도 됨
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.qty = qty;
        return orderItem;
    }
}
