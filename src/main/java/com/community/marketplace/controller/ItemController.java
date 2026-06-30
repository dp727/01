package com.community.marketplace.controller;

import com.community.marketplace.common.ApiResponse;
import com.community.marketplace.dto.ItemCreateRequest;
import com.community.marketplace.dto.ItemDetailResponse;
import com.community.marketplace.dto.ItemSummaryResponse;
import com.community.marketplace.dto.ItemUpdateRequest;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.service.ItemService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemDetailResponse> createItem(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ItemCreateRequest request) {
        return ApiResponse.success(itemService.createItem(userId, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemDetailResponse> getItemDetail(@PathVariable Long id) {
        return ApiResponse.success(itemService.getItemDetail(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<ItemSummaryResponse>> listItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(itemService.listItems(keyword, categoryId, page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<ItemDetailResponse> updateItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ItemUpdateRequest request) {
        return ApiResponse.success(itemService.updateItem(id, userId, request));
    }

    @PostMapping("/{id}/off-shelf")
    public ApiResponse<Void> offShelfItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        itemService.offShelfItem(id, userId);
        return ApiResponse.success("已下架", null);
    }

    @PostMapping("/{id}/on-shelf")
    public ApiResponse<Void> onShelfItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        itemService.onShelfItem(id, userId);
        return ApiResponse.success("已重新上架", null);
    }

    @PostMapping("/{id}/claim")
    public ApiResponse<Void> claimItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        itemService.claimItem(id, userId);
        return ApiResponse.success("已领取", null);
    }
}
