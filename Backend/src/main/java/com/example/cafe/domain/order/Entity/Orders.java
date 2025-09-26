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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long id;

    @Column(name = "deliveryStatus")
    private int deliveryStatus;

    private String customerEmail;
    private LocalDateTime orderDate;

    private String address;

    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Orders() {}

    public Orders(String customerEmail, String address){
        this.customerEmail = customerEmail;
        this.orderDate = LocalDateTime.now();
        this.address = address;
        this.deliveryStatus = 0;
    }

    public static Orders createOrder(String customerEmail, String address, List<OrderItem> orderItems){
        Orders orders = new Orders(customerEmail, address);
        for(OrderItem orderItem : orderItems){
            orders.getOrderItems().add(orderItem);
            orderItem.setOrder(orders); //DB에는 적용되는데 영속성 컨텍스트에도 업로드를 해야합니다. jpa가 영속성 컨텍스트부터 봅니다.
        }
        return orders;
    }

    public void setTotalPrice(){
        this.totalPrice = this
                .getOrderItems()
                .stream()
                .mapToInt(m ->
                        m.getItem().getPrice() * m.getQty()
                )
                .sum();
    }

    public int calculateCurrentDeliveryStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderTime = this.orderDate;

        LocalDateTime today2PM = now.withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime yesterday2PM = today2PM.minusDays(1);

        LocalDateTime orderPlus2Days = orderTime.plusDays(2);

        if (now.isAfter(orderPlus2Days)) {
            return 2;
        } else if (orderTime.isAfter(yesterday2PM) && orderTime.isBefore(today2PM)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public void updateDeliveryStatus() {
        this.deliveryStatus = calculateCurrentDeliveryStatus();
    }

    public void updateOrderDate(LocalDateTime time){
        this.orderDate = time;
    }
}
