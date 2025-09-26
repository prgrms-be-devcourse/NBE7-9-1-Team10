package com.example.cafe.domain.order.Service;

import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Dto.FindAllOrderByEmailResponse;
import com.example.cafe.domain.order.Dto.FindAllOrderResponse;
import com.example.cafe.domain.order.Dto.OrderCreateRequest;
import com.example.cafe.domain.order.Dto.OrderCreateResponse;
import com.example.cafe.domain.order.Entity.OrderItem;
import com.example.cafe.domain.order.Entity.Orders;
import com.example.cafe.domain.order.Repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request){


        //Map<Long, Item>으로 구한다음 매핑하면 쿼리 하나로 가능
        Map<Long, Item> itemMap = itemRepository.findAll().stream()
                .collect(Collectors.toMap(Item::getItemId, item -> item));
        //쿼리 1개로 아이템 목록 찾기 (최적화)
        //id에 대응하는 item 객체 Map 반환

        List<OrderItem> orderItems = request
                .getItems()
                .stream()
                .map(m -> OrderItem.createOrderItem(itemMap.get(m.getId()), m.getQty()) //itemdto에 대응하는 item 객체만 꺼내오기
                )
                .collect(Collectors.toList());

        Orders order = Orders.createOrder(request.getEmail(), request.getAddress(), orderItems); //item들 다 담은 orderitems order로 저장
        orderRepository.save(order); //cascade로 인하여 orderitem 자동으로 생성

        return new OrderCreateResponse(order); //DTO로 옮겨서 반환
    }

    @Transactional
    public FindAllOrderResponse findAllOrders(){
        List<Orders> orders = orderRepository.OptimizedFindAllOrders();
        return new FindAllOrderResponse(orders);
    }

    @Transactional
    public FindAllOrderResponse findOrdersByDeliveryStatus(int deliveryStatus){
        List<Orders> orders = orderRepository.OptimizedFindAllOrders();

        List<Orders> filteredOrders = orders.stream()
                .filter(order -> order.calculateCurrentDeliveryStatus() == deliveryStatus)
                .collect(Collectors.toList());

        return new FindAllOrderResponse(filteredOrders);
    }

    @Transactional
    public FindAllOrderByEmailResponse findAllOrderByEmailResponse(String email){
        List<Orders> orders = orderRepository.OptimizedFindAllOrdersByEmail(email);
        return new FindAllOrderByEmailResponse(email, orders);
    }
}
