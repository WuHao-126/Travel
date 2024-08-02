package com.Travel.vo.param.blog;

import lombok.Data;

@Data
public class FeedBlogParam {
    private Long max;
    private Integer offset;
    private Integer pageSize;
}
