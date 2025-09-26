package com.example.cafe.domain.item.controller;

import com.example.cafe.domain.item.dto.ItemCreateRequest;
import com.example.cafe.domain.item.dto.ItemResponse;
import com.example.cafe.domain.item.dto.ItemUpdateRequest;
import com.example.cafe.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "상품 관리", description = "상품 CRUD API")
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "상품 단일 조회", description = "특정 상품 조회합니다")
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long itemId) {
        ItemResponse item = itemService.getItem(itemId);
        return ResponseEntity.ok(item);
    }

    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다")
    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(value = "User-Email", required = false) String userEmail,
            @RequestBody ItemCreateRequest request) {

        if (!"admin@email.com".equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "관리자가 아닙니다. 권한이 필요합니다."));
        }

        ItemResponse item = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @Operation(summary = "상품 수정", description = "기존 상품 정보를 수정합니다")
    @PutMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(value = "User-Email", required = false) String userEmail,
            @PathVariable Long itemId,
            @RequestBody ItemUpdateRequest request) {

        if (!"admin@email.com".equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "관리자가 아닙니다. 권한이 필요합니다."));
        }

        ItemResponse item = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(item);
    }

    @Operation(summary = "상품 삭제", description = "상품의 Status를 수정합니다")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(
            @RequestHeader(value = "User-Email", required = false) String userEmail,
            @PathVariable Long itemId) {

        if (!"admin@email.com".equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "관리자가 아닙니다. 권한이 필요합니다."));
        }

        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
