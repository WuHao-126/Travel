package com.Travel.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户信息")
@TableName("tb_admin")
public class User {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID",required = true)
    private Integer id;
    private String username;
    private String password;
    private Integer power;
    private String nickName;
    private String userPhoto;
    private Integer grade;
    private Integer fans;
    private Integer experience;
}
