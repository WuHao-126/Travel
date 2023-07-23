package com.Travel.controller;

import com.Travel.dao.pojo.Route;
import com.Travel.dao.pojo.Scenic;
import com.Travel.server.RouteService;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import com.Travel.vo.param.RouteParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private RouteService routeService;

    /**
     * TODO 添加景区路线
     * @param route
     * @return
     */
    @PostMapping()
    public Result addRoute(@RequestBody Route route){
        route.setViews(0);
        return routeService.addRoute(route);
    }

    /**
     * TODO 搜索所有路线并分页
     * @param pageParam
     * @return
     */
    @PostMapping("/page")
    public Result selectAllRoute(@RequestBody PageParam pageParam){
        return routeService.selectAllRoute(pageParam);
    }
    @PostMapping("/aim")
    public Result aimSelectRoute(@RequestBody RouteParam routeParam){
        return routeService.aimSelectRoute(routeParam);
    }

    /**
     * TODO 根据ID查询路线
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Route selectByIdRoute(@PathVariable("id") Integer id){
        return routeService.selectByIdRoute(id);
    }

    /**
     * TODO 查询热点路线
     * @return
     */
    @GetMapping("/hot")
    public Result selectHotRoute(){
        return routeService.selectHotRoute();
    }

    /**
     * TODO 根据ID删除路线
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteRoute(@PathVariable("id") Integer id){
        return routeService.deleteRoute(id);
    }

    /**
     * TODO 修改路线
     * @param route
     * @return
     */
    @PutMapping
    public Result updateRoute(@RequestBody Route route){
        return routeService.updateRoute(route);
    }

}
