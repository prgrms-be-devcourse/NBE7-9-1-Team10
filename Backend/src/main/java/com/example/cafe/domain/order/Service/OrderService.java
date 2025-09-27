package com.example.cafe.domain.order.Service;

import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import com.example.cafe.domain.order.Dto.*;
import com.example.cafe.domain.order.Entity.OrderItem;
import com.example.cafe.domain.order.Entity.Orders;
import com.example.cafe.domain.order.Repository.OrderRepository;
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

        /*
          issue #76에 의해 추가된 로직

          기존 방식: OrderItems의 item 추가를 위해
          request시 받아오는 여러개의 item들을 리스트로 묶어 전체 item findAll후
          loop하여 request에 해당하는 아이템 객체들만 뽑아서 추가했는데
          이러면 데이터가 커질 경우 loop횟수가 많아질것 같아서 안좋아 보입니다.

          수정 : 특정 item id만 리스트로 따와서 in절로 필요한 item객체만 찾은 후 담았습니다.
         */
        List<Long> itemIds = request.getItems().stream().map(m -> m.getId()).toList();

        //Map<Long, Item>으로 구한다음 매핑하면 쿼리 하나로 가능
        Map<Long, Item> itemMap = itemRepository.findSpecificItems(itemIds).stream()
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
        order.setTotalPrice();
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

    public OrderSalesResponse findOrderSales() {
        List<ItemSalesDto> itemSalesDtos= orderRepository.findItemSalesRaw()
                .stream()
                .map((row) ->{
                    return new ItemSalesDto((Long)row[0], (String) row[1], (Long) row[2]);
                })
                .toList();
        Long totalPrice = orderRepository.findTotalSale();
        return new OrderSalesResponse(totalPrice,itemSalesDtos);
    }
}
