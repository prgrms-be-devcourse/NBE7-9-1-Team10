package com.example.domain.order.Repository;

import com.example.domain.order.Entity.OrderItem;
import com.example.domain.order.Entity.Orders;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Orders, Long> {



}

