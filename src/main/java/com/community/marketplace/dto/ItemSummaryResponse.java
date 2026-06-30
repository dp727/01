package com.community.marketplace.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemSummaryResponse {

    private Long id;
    private String title;
    private BigDecimal price;
    private String coverImage;
    private String community;
    private LocalDateTime createdAt;
    private String categoryName;
}
