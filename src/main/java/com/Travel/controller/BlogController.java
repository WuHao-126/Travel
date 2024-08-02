package com.Travel.controller;

import com.Travel.dao.pojo.Blog;
import com.Travel.server.BlogService;
import com.Travel.vo.BlogVo;
import com.Travel.vo.Result;
import com.Travel.vo.param.blog.DeleteBlogParam;
import com.Travel.vo.param.blog.FeedBlogParam;
import com.Travel.vo.param.common.IdParam;
import com.Travel.vo.param.common.PageParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    /**
     * 搜索热门博客
     * @param pageParam
     * @return
     */
    @PostMapping("/hot")
    public Result selectHotBlog(@RequestBody PageParam pageParam){
        return blogService.selectHotBlog(pageParam);
    }

    /**
     * 搜索最新博客
     * @param pageParam
     * @return
     */
    @PostMapping("/new")
    public Result selectnewBlog(@RequestBody PageParam pageParam){
        return blogService.selectnewBlog(pageParam);
    }

    @PostMapping("delete")
    public Result deleteBlog(@RequestBody DeleteBlogParam deleteBlogParam, HttpServletRequest request){
        return blogService.deleteBlog(deleteBlogParam,request);
    }

    @PostMapping("/userid")
    public Result selectBlogByUserId(@RequestBody IdParam idParam){
        return blogService.selectBlogByUserId(idParam);
    }
    /**
     * 添加博客
     * @param blog
     * @return
     */
    @PostMapping()
    public Result addBlog(@RequestBody Blog blog,HttpServletRequest servletRequest){
        if(blog==null){
            throw new RuntimeException("添加博客参数错误");
        }
        return blogService.addBlog(blog,servletRequest);
    }
    /**
     * 搜索单个博客
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BlogVo selectBlogById(@PathVariable("id") Integer id){
        return blogService.selectBlogById(id);
    }

    /**
     * 根据标题搜索博客
     * @param title
     * @return
     */
    @GetMapping("/title/{title}")
    public Result selectTitleBlog(@PathVariable("title") String title){
        return blogService.selectTitleBlog(title);
    }

    /**
     * 博客总数
     * @return
     */
    @GetMapping("/total")
    public Result blogTotal(){
        return blogService.blogTotal();
    }

    /**
     * 用户等级排行
     * @return
     */
    @GetMapping("/arrange")
    public Result userGradeArrange(){
        return blogService.UserGradeArrange();
    }

    @PostMapping("/feed/blog")
    public Result feedBlog(@RequestBody FeedBlogParam feedBlogParam, HttpServletRequest servletRequest){
        Long max = feedBlogParam.getMax();
        Integer offset = feedBlogParam.getOffset();
        Integer pageSize = feedBlogParam.getPageSize();
        return blogService.feedBlog(max,offset,pageSize,servletRequest);
    }

}
