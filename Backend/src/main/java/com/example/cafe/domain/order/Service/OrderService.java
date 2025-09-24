package com.example.cafe.domain.order.Service;

import com.example.cafe.domain.order.Dto.OrderCreateRequest;
import com.example.cafe.domain.order.Dto.OrderCreateResponse;
import com.example.cafe.domain.order.Entity.OrderItem;
import com.example.cafe.domain.order.Entity.Orders;
import com.example.cafe.domain.order.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request){


        List<OrderItem> orderItems = request
                .getItems()
                .stream()
                .map(m -> new OrderItem(m.getItemId(), m.getQty())) // 이부분 어떻게 DTO로 바꿔야할까요? Item객체로 받자니 쿼리를 사용해야할것 같은데, 그냥 int id로 써도 되지 않나 싶긴합니다
                .collect(Collectors.toList());

        Orders order = Orders.createOrder(request.getEmail(), request.getTotalPrice(), request.getAddress(), orderItems);//item 필요함. item 테이블 가져와
        orderRepository.save(order);


    }

}
