package com.Travel.dao.mapper;

import com.Travel.dao.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    void addBlogViews(Integer id);
}
