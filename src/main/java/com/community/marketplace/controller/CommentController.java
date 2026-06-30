package com.community.marketplace.controller;

import com.community.marketplace.common.ApiResponse;
import com.community.marketplace.dto.CommentRequest;
import com.community.marketplace.dto.CommentResponse;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.service.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse> createComment(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CommentRequest request) {
        return ApiResponse.success(commentService.createComment(userId, request));
    }

    @GetMapping("/item/{itemId}")
    public ApiResponse<PageResponse<CommentResponse>> getItemComments(
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(commentService.getItemComments(itemId, page, size));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") Long userId) {
        commentService.deleteComment(commentId, userId);
        return ApiResponse.success("删除成功", null);
    }
}
