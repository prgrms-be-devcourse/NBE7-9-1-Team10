package com.example.cafe.domain.order.Repository;

import com.example.cafe.domain.order.Entity.OrderItem;
import com.example.cafe.domain.order.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Orders, Long> {

    @Query("select o from Orders o join fetch o.orderItems oi join fetch oi.item i")
    List<Orders> OptimizedFindAllOrders();

}

