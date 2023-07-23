package com.Travel.dao.mapper;

import com.Travel.dao.pojo.Information;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InformationMapper extends BaseMapper<Information> {
    void addInformationViews(Integer id);
}
