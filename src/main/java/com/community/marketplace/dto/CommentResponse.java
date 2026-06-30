package com.community.marketplace.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private Long itemId;
    private LocalDateTime createdAt;
}
