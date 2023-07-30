package com.Travel.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVo implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID",required = true)
    private Integer id;
    /**
     * 用户权限
     * TODO 待删除
     */
    private Integer power;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String userPhoto;
    /**
     * 用户等级
     */
    private Integer grade;
    /**
     * 用户粉丝数量
     */
    private Integer fans;
    /**
     * 用户现有经验
     */
    private Integer experience;
    /**
     * json类型的字符串
     */
    private String tags;
    /**
     * 用户个人描述
     */
    private String description;
    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
