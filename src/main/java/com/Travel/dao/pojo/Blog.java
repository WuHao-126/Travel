package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_blog")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer user_id;
    private Date createTime;
    private String content_html;
    private String content;
    private Integer views;
    private String photo;
}
