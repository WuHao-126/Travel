package com.Travel.vo.param;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
    private String code;
}
