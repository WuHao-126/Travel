package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_scenic")
public class Scenic {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String sname;
    private String type;
    private Integer price;
    private String area;
    private String areaDetailed;
    private Date openTime;
    private String detailedText_html;
    private String detailedText;
    private Integer views;
    private String photo;
    private Integer level;
    private Integer sort;
}
