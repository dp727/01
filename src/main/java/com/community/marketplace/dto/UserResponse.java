package com.community.marketplace.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String nickname;
    private String avatar;
    private String location;
    private String buildingNumber;
    private LocalDateTime createdAt;
}
