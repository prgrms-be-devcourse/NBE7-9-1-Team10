package com.example.cafe.domain.item.service;

import com.example.cafe.domain.item.dto.ItemCreateRequest;
import com.example.cafe.domain.item.dto.ItemResponse;
import com.example.cafe.domain.item.dto.ItemUpdateRequest;
import com.example.cafe.domain.item.entity.Item;
import com.example.cafe.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    // 1. 상품 목록 조회
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    // 2. 상품 상세 조회
    public ItemResponse getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemId));
        return ItemResponse.from(item);
    }

    // 3. 상품 생성
    @Transactional
    public ItemResponse createItem(ItemCreateRequest request) {
        Item item = Item.builder()
                .itemName(request.getItemName())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .build();

        Item savedItem = itemRepository.save(item);
        return ItemResponse.from(savedItem);
    }

    // 4. 상품 수정
    @Transactional
    public ItemResponse updateItem(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemId));

        if (request.getItemName() != null) {
            item.setItemName(request.getItemName());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            item.setImageUrl(request.getImageUrl());
        }

        Item updatedItem = itemRepository.save(item);
        return ItemResponse.from(updatedItem);
    }

    // 5. 상품 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemId));

        item.setStatus("noQty");
        itemRepository.save(item);
    }
}
