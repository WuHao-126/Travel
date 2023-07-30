package com.Travel.server.impl;

import cn.hutool.core.date.DateTime;
import com.Travel.dao.mapper.BlogMapper;
import com.Travel.dao.pojo.Blog;
import com.Travel.server.BlogService;
import com.Travel.server.UserService;
import com.Travel.util.JWTUtils;
import com.Travel.vo.*;
import com.Travel.vo.param.DeleteBlogParam;
import com.Travel.vo.param.IdParam;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public Result selectBlogByUserId(IdParam idParam) {
        Integer userId=idParam.getId();
        if(userId<=0){
            throw new RuntimeException("参数为空");
        }
        List<Blog> blogList = query().eq("user_id", userId).list();
        return Result.success(blogList);
    }

    @Override
    public Result deleteBlog(DeleteBlogParam deleteBlogParam, HttpServletRequest servletRequest) {
        UserVo userVo = (UserVo)servletRequest.getSession().getAttribute(SystemConstants.CURRENT_LOGIN_USER);
        if(userVo==null){
            return null;
        }
        Integer userId = deleteBlogParam.getUserId();
        Integer blogId = deleteBlogParam.getBlogId();
        if(userVo.getPower()!=1 && userVo.getId()!=userId){
            throw new RuntimeException("用户异常操作，操作用户id为"+userId);
        }
        if(blogId<=0){
            throw new RuntimeException("");
        }
        int i = blogMapper.deleteById(blogId);
        if(i>0){
            return Result.success(true);
        }
        return Result.fail(ErrorCode.DELETE_ERROR.getCode(),ErrorCode.DELETE_ERROR.getMsg());
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
