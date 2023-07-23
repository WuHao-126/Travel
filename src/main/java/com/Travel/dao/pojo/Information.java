package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_information")
public class Information {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String type;
    private Date publicTime;
    private Integer views;
    private String detailedText_html;
    private String detailedText;
    private String photo;
    private Integer sort;
}
