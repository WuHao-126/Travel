package com.Travel.server.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.Travel.dao.mapper.BlogMapper;
import com.Travel.dao.mapper.UserMapper;
import com.Travel.dao.pojo.Blog;
import com.Travel.server.BlogService;
import com.Travel.server.UserService;
import com.Travel.vo.*;
import com.Travel.vo.param.blog.DeleteBlogParam;
import com.Travel.vo.param.common.IdParam;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.common.ScrollResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class BlogServiceimpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
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
    public Result addBlog(Blog blog,HttpServletRequest servletRequest) {
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        if(userVo==null){
            throw new RuntimeException("用户未登录/用户登录失效");
        }
        Integer userId = userVo.getId();
        blog.setUser_id(userId);
        blog.setViews(0);
        blog.setCreateTime(new Date());
        boolean save = save(blog);
        if(save){
            List<Integer> userFansId = userMapper.getUserFansId(userId);
            //将博客推送到粉丝列表中
            for (Integer id : userFansId) {
               stringRedisTemplate.opsForZSet().add(SystemConstants.FEED_INFORMATION_KEY + id, blog.getId().toString(), System.currentTimeMillis());
            }
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

    @Override
    public Result feedBlog(Long max, Integer offset, Integer pageSize, HttpServletRequest servletRequest) {
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        if(userVo==null){
            throw new RuntimeException("用户未登录/用户登录失效");
        }
        String key=SystemConstants.FEED_INFORMATION_KEY+userVo.getId();
        Set<ZSetOperations.TypedTuple<String>> typedTuples =
                stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, max, offset, pageSize);
        if(typedTuples.isEmpty()){
            return Result.success(null);
        }
        List<String> blogIdLsit=new ArrayList<>(typedTuples.size());
        long minTime=0l;
        int os=1;
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) { //5 4 4 3 3 2
            String value = typedTuple.getValue();
            long score = typedTuple.getScore().longValue();
            blogIdLsit.add(value);
            if(minTime==score){
               os++;
            }else{
                minTime=Long.parseLong(value);
                os=1;
            }
        }
        String idStr = StrUtil.join(",", blogIdLsit);
        List<Blog> blogs = query().in("id", blogIdLsit).last("ORDER BY FIELD(id," + idStr + ")").list();
        ScrollResult<Blog> scrollResult=new ScrollResult<>();
        scrollResult.setList(blogs);
        scrollResult.setMinTime(minTime);
        scrollResult.setOffset(os);
        return Result.success(scrollResult);
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
