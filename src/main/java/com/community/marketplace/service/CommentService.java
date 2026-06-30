package com.community.marketplace.service;

import com.community.marketplace.common.BusinessException;
import com.community.marketplace.dto.CommentRequest;
import com.community.marketplace.dto.CommentResponse;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.entity.Comment;
import com.community.marketplace.entity.Item;
import com.community.marketplace.entity.User;
import com.community.marketplace.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    public CommentResponse createComment(Long userId, CommentRequest request) {
        User user = userService.findById(userId);
        Item item = itemService.findById(request.getItemId());

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .item(item)
                .build();

        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    public PageResponse<CommentResponse> getItemComments(Long itemId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByItemIdOrderByCreatedAtDesc(itemId, pageRequest);
        return toPageResponse(commentPage);
    }

    public PageResponse<CommentResponse> getUserComments(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageRequest);
        return toPageResponse(commentPage);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(404, "留言不存在"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new BusinessException(403, "无权删除他人留言");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .userAvatar(comment.getUser().getAvatar())
                .itemId(comment.getItem().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private PageResponse<CommentResponse> toPageResponse(Page<Comment> commentPage) {
        return PageResponse.<CommentResponse>builder()
                .content(commentPage.getContent().stream().map(this::toResponse).collect(Collectors.toList()))
                .page(commentPage.getNumber())
                .size(commentPage.getSize())
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .build();
    }
}
