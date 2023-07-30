package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_scenic")
public class Scenic implements Serializable {
    /**
     * 景区id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 景区名称
     */
    private String sname;
    /**
     * 景区类型（国内/国外）
     */
    private String type;
    /**
     * 大致花费
     */
    private Integer price;
    /**
     * 所属区域
     */
    private String area;
    /**
     * 所属详细区域
     */
    private String areaDetailed;
    /**
     * 开放时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date openTime;
    /**
     * 介绍内容html格式
     */
    private String detailedText_html;
    /**
     * 介绍内容
     */
    private String detailedText;
    /**
     * 访问量
     */
    private Integer views;
    /**
     * 封面图片
     */
    private String photo;
    /**
     * 景区等级
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer sort;
}
