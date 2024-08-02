package com.Travel.server;

import com.Travel.dao.pojo.Route;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.route.RouteParam;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RouteService extends IService<Route> {
    /**
     * 添加路线信息
     * @param route
     * @return
     */
    Result addRoute(Route route);

    /**
     * 根据分页参数搜索路线
     * @param pageParam
     * @return
     */
    Result selectAllRoute(PageParam pageParam);

    /**
     * 根据id删除路线
     * @param id
     * @return
     */
    Result deleteRoute(Integer id);

    /**
     * 修改路线
     * @param route
     * @return
     */
    Result updateRoute(Route route);

    /**
     * 根据id搜索路线
     * @param id
     * @return
     */
    Route selectByIdRoute(Integer id);

    /**
     * 搜索热门路线
     * @return
     */
    Result selectHotRoute();

    /**
     * 更具路线参数查询路线
     * @param routeParam
     * @return
     */
    Result aimSelectRoute(RouteParam routeParam);
}
