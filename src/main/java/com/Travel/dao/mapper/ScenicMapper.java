package com.Travel.dao.mapper;

import com.Travel.dao.pojo.Scenic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScenicMapper extends BaseMapper<Scenic> {
    void addDetailedScenicViews(Integer id);
}
