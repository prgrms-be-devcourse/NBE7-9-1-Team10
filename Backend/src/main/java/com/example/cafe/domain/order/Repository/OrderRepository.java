package com.example.cafe.domain.order.Repository;

import com.example.cafe.domain.order.Dto.ItemSalesDto;
import com.example.cafe.domain.order.Entity.OrderItem;
import com.example.cafe.domain.order.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Orders, Long> {

    @Query("select o from Orders o join fetch o.orderItems oi join fetch oi.item i")
    List<Orders> OptimizedFindAllOrders();

    @Query("select o from Orders o join fetch o.orderItems oi join fetch oi.item i where o.customerEmail = :email")
    List<Orders> OptimizedFindAllOrdersByEmail(@Param("email") String email);

    // Repository
    @Query("select i.itemId, i.itemName, sum(oi.qty) " +
            "from OrderItem oi join oi.item i " +
            "group by i.itemId, i.itemName")
    List<Object[]> findItemSalesRaw();


    @Query("select sum(oi.qty * i.price) " +
            "from OrderItem oi " +
            "join oi.item i")
    Long findTotalSale();
}

