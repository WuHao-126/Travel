package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("tb_comment")
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_date;
    private Integer article_id;
    private Integer author_id;
    private Integer parent_id;
    private Integer to_uid;
    private Integer level;
    private Integer productType;
}
