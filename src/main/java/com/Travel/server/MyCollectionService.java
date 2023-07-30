package com.Travel.server;

import com.Travel.dao.pojo.MyCollection;
import com.Travel.vo.Result;
import com.Travel.vo.param.CollectionParam;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface MyCollectionService extends IService<MyCollection> {
    /**
     * 搜索登录用户所收藏的景区
     * @param pageParam
     * @return
     */
    Result selectScenicList(PageParam pageParam, HttpServletRequest servletRequest);

    /**
     * 搜索登录用户所收藏的路线
     * @param pageParam
     * @return
     */
    Result selectRouteList(PageParam pageParam, HttpServletRequest servletRequest);

    /**
     * TODO 没用不用写
     * @param pageParam
     * @return
     */
    Result selectBlogList(PageParam pageParam, HttpServletRequest servletRequest);

    /**
     * 添加收藏
     * @param collectionParam
     * @return
     */
    Result addCollection(CollectionParam collectionParam, HttpServletRequest servletRequest);

    /**
     * 删除收藏
     * @param id
     * @return
     */
    Result deleteCollection(Integer id);

    Result getUserCollectionScenic(CollectionParam collectionParam);

    Result getUserCollectionRouter(CollectionParam collectionParam);
}
