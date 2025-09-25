package com.example.cafe.domain.order.Entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Orders {

    @Id
    @GeneratedValue
    @Column(name = "orderId")
    private Long id;

    private String customerEmail;
    private LocalDateTime orderDate;

    private int totalPrice;
    private String address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Orders() {}

    public Orders(String customerEmail, int totalPrice, String address){
        this.customerEmail = customerEmail;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.address = address;
    }

    public static Orders createOrder(String customerEmail, int totalPrice, String address, List<OrderItem> orderItems){
        Orders orders = new Orders(customerEmail, totalPrice, address);
        for(OrderItem orderItem : orderItems){
            orders.getOrderItems().add(orderItem);
        }

        return orders;
    }

}
