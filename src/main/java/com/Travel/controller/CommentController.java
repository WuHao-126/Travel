package com.Travel.controller;

import com.Travel.server.CommentService;
import com.Travel.vo.param.CommentParam;
import com.Travel.vo.Result;
import com.Travel.vo.SystemConstants;
import com.Travel.vo.param.PageParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/{id}/{commentType}")
    public Result selectByIdAllComment(@PathVariable("id")Integer id,@PathVariable("commentType") Integer commentType, @RequestBody PageParam pageParam){
        return commentService.selectByIdAllComment(id,pageParam, commentType);
    }
    @PostMapping("/save")
    public Result addScenicComment(@RequestBody CommentParam commentParam, HttpServletRequest servletRequest){
        return commentService.addComment(commentParam,SystemConstants.SCENIC_COMMETN,servletRequest);
    }
    @DeleteMapping("/{id}")
    public boolean deleteComment(@PathVariable("id") Integer id){
        return commentService.deleteComment(id);
    }

}
