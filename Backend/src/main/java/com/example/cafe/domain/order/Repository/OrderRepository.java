package com.example.cafe.domain.order.Repository;

import com.example.cafe.domain.order.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <Orders, Long> {



}

