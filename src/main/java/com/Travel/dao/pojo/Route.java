package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_route")
public class Route implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String start;
    private String road;
    private String end;
    private Integer views;
    private Integer price;
    private Integer level;
    private String photo;
    private String detailedText_html;
    private String detailedText;
    private Integer sort;
}
