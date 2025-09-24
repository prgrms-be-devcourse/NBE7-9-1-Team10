package com.example.cafe.domain.order.Repository;

import com.example.cafe.domain.order.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Orders, Long> {



}

