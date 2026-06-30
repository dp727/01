package com.community.marketplace.dto;

import javax.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemUpdateRequest {

    @Size(max = 100, message = "标题长度不能超过100")
    private String title;

    @Size(max = 2000, message = "描述长度不能超过2000")
    private String description;

    private BigDecimal price;

    private String coverImage;

    private String images;

    @Size(max = 100, message = "小区名称长度不能超过100")
    private String community;

    private Long categoryId;
}
