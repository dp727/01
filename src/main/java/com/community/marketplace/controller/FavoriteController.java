package com.community.marketplace.controller;

import com.community.marketplace.common.ApiResponse;
import com.community.marketplace.dto.ItemSummaryResponse;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{itemId}")
    public ApiResponse<Void> addFavorite(
            @PathVariable Long itemId,
            @RequestHeader("X-User-Id") Long userId) {
        favoriteService.addFavorite(userId, itemId);
        return ApiResponse.success("收藏成功", null);
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<Void> removeFavorite(
            @PathVariable Long itemId,
            @RequestHeader("X-User-Id") Long userId) {
        favoriteService.removeFavorite(userId, itemId);
        return ApiResponse.success("取消收藏成功", null);
    }

    @GetMapping("/check/{itemId}")
    public ApiResponse<Map<String, Boolean>> isFavorite(
            @PathVariable Long itemId,
            @RequestHeader("X-User-Id") Long userId) {
        boolean favorite = favoriteService.isFavorite(userId, itemId);
        return ApiResponse.success(Collections.singletonMap("favorited", favorite));
    }

    @GetMapping
    public ApiResponse<PageResponse<ItemSummaryResponse>> getUserFavorites(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(favoriteService.getUserFavorites(userId, page, size));
    }
}
