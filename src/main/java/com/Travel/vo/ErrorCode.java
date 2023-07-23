package com.Travel.vo;

public enum ErrorCode {
    PARAMS_ERROR(10001,"参数有误"),
    PARAMS_EMPTY(10002,"参数为空"),
    ACCOUNT_PWD_NOT_EXIST(10003,"用户名或密码错误"),
    NOT_LOGIN(10005,"未登录"),
    NO_DATE(10004,"暂无数据"),
    INSERT_ERROR(2001,"添加失败"),
    DELETE_ERROR(2002,"删除失败"),
    UPDATE_ERROR(2003,"修改失败"),
    TOKEN_ERROR(1003,"token不合法"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    ACCOUNT_EXIST(1004,"账户已经被注册"),
    NO_LOGIN(90002,"未登录"),
    ;
    private Integer code;
    private String msg;
    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
