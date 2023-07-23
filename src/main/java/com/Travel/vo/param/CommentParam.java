package com.Travel.vo.param;

import lombok.Data;

@Data
public class CommentParam {
    private Integer productType;
    private Integer article_id;
    private String content;
    private Integer parent_id;
    private Integer to_uid;
}
