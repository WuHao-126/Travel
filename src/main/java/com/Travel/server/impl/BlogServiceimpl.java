package com.Travel.server.impl;

import cn.hutool.core.date.DateTime;
import com.Travel.dao.mapper.BlogMapper;
import com.Travel.dao.pojo.Blog;
import com.Travel.server.BlogService;
import com.Travel.server.UserService;
import com.Travel.util.JWTUtils;
import com.Travel.vo.BlogVo;
import com.Travel.vo.ErrorCode;
import com.Travel.vo.Result;
import com.Travel.vo.UserVo;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceimpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result selectHotBlog(PageParam pageParam) {
        Page<Blog> page = query()
                .orderByDesc("views")
                .page(new Page<Blog>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Blog> records = page.getRecords();
        if(!records.isEmpty()){
            return Result.success(copyList(records));
        }
        return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
    }
    @Override
    public Result selectnewBlog(PageParam pageParam) {
        Page<Blog> page = query().orderByDesc("createTime").page(new Page<Blog>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Blog> records = page.getRecords();
        if(!records.isEmpty()){
            return Result.success(copyList(records));
        }
        return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
    }

    @Override
    public BlogVo selectBlogById(Integer id) {
        Blog blog = query().eq("id", id).one();
        if(blog!=null){
            blogMapper.addBlogViews(blog.getId());
        }
        BlogVo blogVo = copy(blog);
        return blogVo;
    }

    @Override
    public Result addBlog(Blog blog) {
        String token = stringRedisTemplate.opsForValue().get("token");
        if(token==null || "".equals(token)){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        Integer userId = (Integer)map.get("userId");
        blog.setUser_id(userId);
        blog.setViews(0);
        blog.setCreateTime(new Date());
        boolean save = save(blog);
        if(save){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.DELETE_ERROR.getMsg());
        }
    }

    @Override
    public Result selectTitleBlog(String title) {
        List<Blog> list = query().eq("title", title).list();
        if(list!=null){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result blogTotal() {
        Integer count = query().count();
        if(count!=0){
            return Result.success(count);
        }else {
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result UserGradeArrange() {
        List<UserVo> voList = userService.userGardeArrange();
        if(!voList.isEmpty()){
            return Result.success(voList);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    public List<BlogVo> copyList(List<Blog> blogList){
        List<BlogVo> BlogVoList=new ArrayList<>();
        for (Blog blog : blogList) {
            BlogVoList.add(copy(blog));
        }
        return BlogVoList;
    }
    public BlogVo copy(Blog blog){
        BlogVo blogVo=new BlogVo();
        UserVo userVo = userService.selectByIdUserVo(blog.getUser_id());
        BeanUtils.copyProperties(blog,blogVo);
        blogVo.setCreateTime(new DateTime(blog.getCreateTime()).toString("yyyy-MM-dd HH:mm"));
        blogVo.setUserVo(userVo);
        return blogVo;
    }
}
