package com.example.cafe.domain.item.controller;

import com.example.cafe.domain.item.dto.ItemCreateRequest;
import com.example.cafe.domain.item.dto.ItemResponse;
import com.example.cafe.domain.item.dto.ItemUpdateRequest;
import com.example.cafe.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 1. 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // 2. 상품 상세 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long itemId) {
        ItemResponse item = itemService.getItem(itemId);
        return ResponseEntity.ok(item);
    }

    // 3. 상품 생성
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemCreateRequest request) {
        ItemResponse item = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    // 4. 상품 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemUpdateRequest request) {
        ItemResponse item = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(item);
    }

    // 5. 상품 삭제
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
