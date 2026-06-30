package com.community.marketplace.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemDetailResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String coverImage;
    private String images;
    private String community;
    private String status;
    private String categoryName;
    private Long categoryId;
    private String publisherNickname;
    private Long publisherId;
    private String publisherAvatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
