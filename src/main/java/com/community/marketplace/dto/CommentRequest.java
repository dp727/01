package com.community.marketplace.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull(message = "物品ID不能为空")
    private Long itemId;

    @NotBlank(message = "留言内容不能为空")
    @Size(max = 1000, message = "留言内容长度不能超过1000")
    private String content;
}
