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
            orderItem.setOrder(orders); //DB에는 적용되는데 영속성 컨텍스트에도 업로드를 해야합니다. jpa가 영속성 컨텍스트부터 봅니다.
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
