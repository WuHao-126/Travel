package com.Travel.server.impl;

import com.Travel.dao.mapper.RouteMapper;
import com.Travel.dao.pojo.Route;
import com.Travel.server.RouteService;
import com.Travel.vo.ErrorCode;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.route.RouteParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceimpl extends ServiceImpl<RouteMapper, Route> implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public Result addRoute(Route route) {
        boolean save = save(route);
        if(save){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
    }

    @Override
    public Result selectAllRoute(PageParam pageParam) {
        Page<Route> page = query().page(new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize()));
        List<Route> records = page.getRecords();
        if(!records.isEmpty()){
            return Result.success(records);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result deleteRoute(Integer id) {
        boolean flag = removeById(id);
        if(flag){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.DELETE_ERROR.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result updateRoute(Route route) {
        Integer id = route.getId();
        if(id==null){
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_EMPTY.getMsg());
        }
        boolean flag = updateById(route);
        if(flag){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
    }

    @Override
    public Route selectByIdRoute(Integer id) {
        Route route = routeMapper.selectById(id);
        if(route!=null){
            routeMapper.addDetailedRouteViews(route.getId());
            return route;
        }
        return null;
    }

    @Override
    public Result selectHotRoute() {
        Page<Route> sort = query().orderByAsc("sort").page(new Page<Route>(1, 8));
        List<Route> list = sort.getRecords();
        if(!list.isEmpty()){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result aimSelectRoute(RouteParam routeParam) {
        List<Route> routes = routeMapper.aimSelectRoute(routeParam);
        System.out.println(routes);
        if(routes!=null){
            return Result.success(routes);
        }
        return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
    }
}
