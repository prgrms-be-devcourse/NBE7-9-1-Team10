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

    private String address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Orders() {}

    public Orders(String customerEmail, String address){
        this.customerEmail = customerEmail;
        this.orderDate = LocalDateTime.now();
        this.address = address;
    }

    public static Orders createOrder(String customerEmail, String address, List<OrderItem> orderItems){
        Orders orders = new Orders(customerEmail, address);
        for(OrderItem orderItem : orderItems){
            orders.getOrderItems().add(orderItem);
        }

        return orders;
    }

    public int totalPrice(){
        return this
                .getOrderItems()
                .stream()
                .mapToInt(m ->
                        m.getItem().getPrice() * m.getQty()
                )
                .sum();
    }

}
