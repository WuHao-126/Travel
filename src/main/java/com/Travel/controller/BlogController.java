package com.Travel.controller;

import com.Travel.dao.pojo.Blog;
import com.Travel.server.BlogService;
import com.Travel.vo.BlogVo;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public Result addBlog(@RequestBody Blog blog){
        return blogService.addBlog(blog);
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

}
