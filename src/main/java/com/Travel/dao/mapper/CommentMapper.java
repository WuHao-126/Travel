package com.Travel.dao.mapper;

import com.Travel.dao.pojo.Comment;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment> selectScenicComment(Integer id, PageParam pageParam);
    List<Comment> selectRouteComment(Integer id, PageParam pageParam);
    List<Comment> selectBlogComment(Integer id, PageParam pageParam);
    List<Comment> selectRouteChilderComment(Integer id);
    List<Comment> selectBlogChilderComment(Integer id);
    Boolean addRouteComment(@RequestBody Comment comment);

}
