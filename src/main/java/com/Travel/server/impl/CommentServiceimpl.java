package com.Travel.server.impl;

import cn.hutool.core.date.DateTime;
import com.Travel.dao.mapper.CommentMapper;
import com.Travel.dao.pojo.Comment;

import com.Travel.server.CommentService;
import com.Travel.server.UserService;
import com.Travel.vo.*;
import com.Travel.vo.param.common.CommentParam;
import com.Travel.vo.param.common.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class CommentServiceimpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;

    private static Integer code;
    @Override
    public Result selectByIdAllComment(Integer id, PageParam pageParam,Integer commentType) {
        Page<Comment> page = query().
                eq("productType", commentType).
                eq("level",1).
                eq("article_id", id).
                orderByDesc("create_date")
                .page(new Page<Comment>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Comment> records = page.getRecords();
        if(records!=null){
            return Result.success(copyList(records, commentType));
        }
        return null;
    }

    @Override
    public Result addComment(CommentParam commentParam, Integer commentType, HttpServletRequest servletRequest) {
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        Integer userId = userVo.getId();
        Comment comment = new Comment();
        comment.setArticle_id(commentParam.getArticle_id());
        comment.setAuthor_id(userId);
        comment.setContent(commentParam.getContent());
        comment.setCreate_date(new Date());
        Integer parent = commentParam.getParent_id();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
            comment.setParent_id(0);
        }else{
            comment.setLevel(2);
            comment.setParent_id(parent);
        }
        Integer toUserId = commentParam.getTo_uid();
        comment.setTo_uid(toUserId == null ? 0 : toUserId);
        comment.setProductType(commentParam.getProductType());
        boolean save = save(comment);
        if(save){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
        }
    }

    @Override
    public boolean deleteComment(Integer id) {
        Comment comment = query().eq("id", id).one();
        if(comment==null){
            return false;
        }
        int level=comment.getLevel();
        if(level==2){
            boolean b = removeById(id);
            return b;
        }else{
            List<Comment> list = query().eq("parent_id", id).list();
            for (Comment comment1 : list) {
                boolean b = removeById(comment1.getId());
                if(!b){
                    return false;
                }
            }
            boolean b1 = removeById(id);
            if (b1) return true;
        }
       return false;
    }

    public List<CommentVo> copyList(List<Comment> comments,Integer commentType){
        List<CommentVo> commentVos=new ArrayList<>();
        for (Comment comment : comments) {
            commentVos.add(copy(comment,commentType));
        }
        return commentVos;
    }
    public CommentVo copy(Comment comment,Integer commentType){
        CommentVo commentVo=new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setCreateDate(new DateTime(comment.getCreate_date()).toString("yyyy-MM-dd HH:mm"));
        Integer authorId=comment.getAuthor_id();
        UserVo userVo = userService.selectByIdUserVo(authorId);
        commentVo.setAuthor(userVo);
        List<CommentVo> commentsByParentId = findChildrenComment(comment.getId(),commentType);
        commentVo.setChildrens(commentsByParentId);
        if (comment.getLevel() > 1) {
            Integer toUid = comment.getTo_uid();
            UserVo userVo1 = userService.selectByIdUserVo(toUid);
            commentVo.setToUser(userVo1);
        }
        return commentVo;
    }
    private List<CommentVo> findChildrenComment(Integer id,Integer commentType) {
            List<Comment> comments = query()
                    .eq("parent_id", id)
                    .eq("level", 2)
                    .eq("productType",commentType)
                    .orderByDesc("create_date")
                    .list();
            return copyList(comments,commentType);
    }
}
