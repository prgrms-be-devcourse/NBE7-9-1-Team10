package com.example.cafe;

import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Entity.OrderItem;
import com.example.cafe.domain.order.Entity.Orders;
import com.example.cafe.domain.order.Repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.initDB();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;

        public void initDB(){

            //Item 만들기
            Item item1 = new Item("Item1", 1000, "urls");
            Item item2 = new Item("Item2", 1000, "urls");
            Item item3 = new Item("Item3", 2500, "urls");
            Item item4 = new Item("Item4", 3000, "urls");

            em.persist(item1);
            em.persist(item2);
            em.persist(item3);
            em.persist(item4);
            //

            //orderitem 만들기
            List<OrderItem> orderItemList1 = new ArrayList<>();
            List<OrderItem> orderItemList2 = new ArrayList<>();
            List<OrderItem> orderItemList3 = new ArrayList<>();
            List<OrderItem> orderItemList4 = new ArrayList<>();

            //주문 1
            orderItemList1.add(OrderItem.createOrderItem(item1, 2));
            orderItemList1.add(OrderItem.createOrderItem(item2, 3));
            //주문 2
            orderItemList2.add(OrderItem.createOrderItem(item4, 1));
            //주문 3
            orderItemList3.add(OrderItem.createOrderItem(item3, 1));
            //주문 4
            orderItemList4.add(OrderItem.createOrderItem(item1, 5));
            orderItemList4.add(OrderItem.createOrderItem(item3, 10));
            orderItemList4.add(OrderItem.createOrderItem(item4, 8));
            //

            //order만들기
            Orders order1 = Orders.createOrder("email@example.com", "서울시 강남구..", orderItemList1);

            Orders order2 = Orders.createOrder("asdf@example.com", "경기도 과천시..", orderItemList2);
            Orders order3 = Orders.createOrder("fdsa@example.com", "세종특별자치시..", orderItemList3);
            Orders order4 = Orders.createOrder("1234@example.com", "제주도...", orderItemList4);
            //

            //가격 계산
            order1.setTotalPrice();
            order2.setTotalPrice();
            order3.setTotalPrice();
            order4.setTotalPrice();

            //주문 & 주문상품 일괄 save
            em.persist(order1);
            em.persist(order2);
            em.persist(order3);
            em.persist(order4);

            //시간 계산
            LocalDateTime now = LocalDateTime.now();

            order1.updateDeliveryStatus();
            order2.updateOrderDate(now.minusHours(12));
            order3.updateOrderDate(now.minusHours(24));
            order4.updateOrderDate(now.minusHours(48));

            order2.updateDeliveryStatus();
            order3.updateDeliveryStatus();
            order4.updateDeliveryStatus();
        }
    }
}
