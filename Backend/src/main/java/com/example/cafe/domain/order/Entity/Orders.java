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
        /*
        issue #92에 의해 수정된 내용
        - 전날 14시 이전의 주문은 배송완료 (기존엔 2일전일 경우 배송완료로 확인)
        - 전날 14시 이후 당일 14시 이전은 배송중
        - 당일 14시 이후는 배송준비
        - 위 기준으로 변경
         */
        /*
        issue #114에 의해 수정된 내용
        - 기존 내용은 확인하는 시간에 대해서 충분히 고려하지 않았음
        - 전전날까지 계산에 포함해서 검사해야 간단한 형식의 묶음처리 방식으로 확인 가능함
        - 기획은 전날 14부터 당일 오후 14시까지의 주문을 묶어서 처리(배송)하기
        - 현재 시간이 14시 기준으로 이전인지 이후인지 확인하고 처리할 필요가 있다.
        - 오늘 14시 이전에 확인하는 경우
            - 전전날 14시 이전 주문 -> 배송완료
            - 전전날 14시 이후 ~ 전날 14시 이전 -> 배송중
            - 전날 14시 이후~ -> 배송준비
        - 오늘 14시 이후에 확인하는 경우
            - 전날 14시 이전 주문 -> 배송완료
            - 전날 14시 이후 ~ 당일 14시 이전 -> 배송중
            - 당일 14시 이후~ -> 배송준비
         */
        LocalDateTime now                   = LocalDateTime.now();
        LocalDateTime today2pm              = now.withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime yesterday2pm          = today2pm.minusDays(1);
        LocalDateTime dayBeforeYesterday2pm = yesterday2pm.minusDays(1);

        if (now.isBefore(today2pm)) {
            if(orderDate.isBefore(dayBeforeYesterday2pm)) return 2;
            if(orderDate.isBefore(yesterday2pm)) return 1;
            return 0;
        }
        else {
            if(orderDate.isBefore(yesterday2pm)) return 2;
            if(orderDate.isBefore(today2pm)) return 1;
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
