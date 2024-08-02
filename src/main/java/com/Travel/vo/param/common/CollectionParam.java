package com.Travel.vo.param.common;

import lombok.Data;

@Data
public class CollectionParam extends PageParam{
    private Integer userId;
    private Integer productId;
    private Integer type;
}
