package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_carouselchart")
public class Carousel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String imgurl;
}
