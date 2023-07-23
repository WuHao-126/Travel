package com.Travel.vo;

import com.Travel.dao.pojo.User;
import lombok.Data;

import java.util.Date;

@Data
public class BlogVo {
    private Integer id;
    private String title;
    private UserVo userVo;
    private String createTime;
    private String content;
    private Integer views;
    private String photo;
}
