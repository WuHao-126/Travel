package com.Travel.server;

import com.Travel.dao.pojo.Comment;
import com.Travel.vo.param.CommentParam;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface CommentService extends IService<Comment> {
    /**
     * 根据文章的种类和文章的id搜索该文章下的所有评论
     * @param id
     * @param pageParam
     * @param commentType
     * @return
     */
    Result selectByIdAllComment(Integer id, PageParam pageParam,Integer commentType);

    /**
     * 根据文章的种类和文章的id向该文章添加评论
     * @param commentParam
     * @param commentType
     * @return
     */
    Result addComment(CommentParam commentParam, Integer commentType, HttpServletRequest httpServletRequest);

    /**
     * 删除评论
     * @param id
     * @return
     */
    boolean deleteComment(Integer id);
}
