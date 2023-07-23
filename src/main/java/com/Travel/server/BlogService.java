package com.Travel.server;

import com.Travel.dao.pojo.Blog;
import com.Travel.vo.BlogVo;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BlogService extends IService<Blog> {
    /**
     * 搜索热门景区
     * @param pageParam 分页参数
     * @return
     */
    Result selectHotBlog(PageParam pageParam);

    /**
     * 搜索热门路线
     * @param pageParam 分页参数
     * @return
     */
    Result selectnewBlog(PageParam pageParam);

    /**
     * 根据ID查询景区信息
     * @param id
     * @return
     */
    BlogVo selectBlogById(Integer id);

    /**
     * 添加景区
     * @param blog
     * @return
     */
    Result addBlog(Blog blog);

    /**
     * 根据名称搜索景区信息
     * @param title
     * @return
     */
    Result selectTitleBlog(String title);

    /**
     * 搜索现有添加景区的总数
     * @return
     */
    Result blogTotal();

    /**
     * 用户等级排行
     * @return
     */
    Result UserGradeArrange();
}
