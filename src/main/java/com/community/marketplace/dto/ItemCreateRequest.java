package com.community.marketplace.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100")
    private String title;

    @Size(max = 2000, message = "描述长度不能超过2000")
    private String description;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private String coverImage;

    private String images;

    @Size(max = 100, message = "小区名称长度不能超过100")
    private String community;

    @NotNull(message = "分类不能为空")
    private Long categoryId;
}
