package com.Travel.vo.param.user;

import lombok.Data;

@Data
public class UpdateUserParam {
    private Integer id;
    private String userPhoto;
    private String nickName;
    private String description;
}
