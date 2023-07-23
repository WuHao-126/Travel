package com.Travel.vo;

import com.Travel.dao.pojo.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    private Integer id;//

    private UserVo author;

    private String content;//

    private List<CommentVo> childrens;

    private String createDate;//

    private Integer level;//

    private UserVo toUser;
}
