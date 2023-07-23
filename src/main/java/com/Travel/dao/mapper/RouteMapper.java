package com.Travel.dao.mapper;

import com.Travel.dao.pojo.Route;
import com.Travel.vo.param.RouteParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteMapper extends BaseMapper<Route> {
    void addDetailedRouteViews(Integer id);
    List<Route> aimSelectRoute(RouteParam routeParam);
}
