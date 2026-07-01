package com.community.marketplace.controller;

import com.community.marketplace.common.ApiResponse;
import com.community.marketplace.dto.CommentResponse;
import com.community.marketplace.dto.ItemSummaryResponse;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.dto.UserCreateRequest;
import com.community.marketplace.dto.UserLocationRequest;
import com.community.marketplace.dto.UserResponse;
import com.community.marketplace.dto.UserUpdateRequest;
import com.community.marketplace.service.CommentService;
import com.community.marketplace.service.FavoriteService;
import com.community.marketplace.service.ItemService;
import com.community.marketplace.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ItemService itemService;
    private final FavoriteService favoriteService;
    private final CommentService commentService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }

    @PostMapping("/{id}/location")
    public ApiResponse<UserResponse> updateUserLocation(
            @PathVariable Long id,
            @Valid @RequestBody UserLocationRequest request) {
        return ApiResponse.success(userService.updateUserLocation(
                id, request.getLatitude(), request.getLongitude(), request.getLocation()));
    }

    @GetMapping("/{id}/location")
    public ApiResponse<UserResponse> getUserLocation(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @GetMapping("/{userId}/items")
    public ApiResponse<PageResponse<ItemSummaryResponse>> getUserItems(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(itemService.listUserItems(userId, status, page, size));
    }

    @GetMapping("/{userId}/favorites")
    public ApiResponse<PageResponse<ItemSummaryResponse>> getUserFavorites(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(favoriteService.getUserFavorites(userId, page, size));
    }

    @GetMapping("/{userId}/comments")
    public ApiResponse<PageResponse<CommentResponse>> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(commentService.getUserComments(userId, page, size));
    }
}
