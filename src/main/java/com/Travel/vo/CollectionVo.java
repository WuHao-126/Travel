package com.Travel.vo;

import lombok.Data;

import java.util.List;

@Data
public class CollectionVo<T> {
    private T t;
}
