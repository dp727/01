package com.community.marketplace.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    private String avatar;

    @Size(max = 100, message = "位置长度不能超过100")
    private String location;

    @Size(max = 20, message = "楼栋号长度不能超过20")
    private String buildingNumber;
}
