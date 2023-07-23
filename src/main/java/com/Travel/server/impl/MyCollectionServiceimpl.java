package com.Travel.server.impl;

import com.Travel.dao.mapper.MyCollectionMapper;
import com.Travel.dao.mapper.UserMapper;
import com.Travel.dao.pojo.MyCollection;
import com.Travel.dao.pojo.Route;
import com.Travel.dao.pojo.Scenic;
import com.Travel.dao.pojo.User;
import com.Travel.server.MyCollectionService;
import com.Travel.server.RouteService;
import com.Travel.server.ScenicService;
import com.Travel.server.UserService;
import com.Travel.util.JWTUtils;
import com.Travel.util.UserThreadLocal;
import com.Travel.vo.*;
import com.Travel.vo.param.CollectionParam;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyCollectionServiceimpl extends ServiceImpl<MyCollectionMapper, MyCollection> implements MyCollectionService {

    @Autowired
    private ScenicService scenicService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private UserService userService;
    @Autowired
    private MyCollectionMapper myCollectionMapper;
    @Override
    public Result selectScenicList(PageParam pageParam, HttpServletRequest servletRequest) {
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        Integer userId = userVo.getId();
        Page<MyCollection> page = query()
                .eq("type", 0)
                .eq("userId", userId)
                .page(new Page<MyCollection>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<MyCollection> records = page.getRecords();
        if(!records.isEmpty()){
            return Result.success(copyList(records,Scenic.class));
        }else {
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectRouteList(PageParam pageParam, HttpServletRequest servletRequest) {
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        Integer userId = userVo.getId();
        Page<MyCollection> page = query()
                .eq("type", 1)
                .eq("userId", userId)
                .page(new Page<MyCollection>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<MyCollection> records = page.getRecords();
        if(!records.isEmpty()){
            return Result.success(copyList(records,Route.class));
        }else {
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectBlogList(PageParam pageParam, HttpServletRequest servletRequest) {
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        Integer userId = userVo.getId();
        Page<MyCollection> page = query()
                .eq("productId", 2)
                .eq("userId", userId)
                .page(new Page<MyCollection>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<MyCollection> records = page.getRecords();
        if(!records.isEmpty()){
            return Result.success(records);
        }else {
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result addCollection(CollectionParam collectionParam, HttpServletRequest servletRequest) {
        Integer productId = collectionParam.getProductId();
        Integer type = collectionParam.getType();
        if(productId==0 || type<0 || type>2){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        Integer userId = userVo.getId();
        MyCollection myCollection = query()
                .eq("userId", userId)
                .eq("productId", collectionParam.getProductId())
                .eq("type", type)
                .one();
        if (myCollection==null){
            MyCollection my = new MyCollection();
            my.setProductId(collectionParam.getProductId());
            my.setType(collectionParam.getType());
            my.setUserId(userId);
            boolean save = save(my);
            if(save){
                return Result.success(null);
            }else{
                return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
            }
        }else{
            deleteCollection(myCollection.getId());
            return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
        }
    }
    @Override
    public Result deleteCollection(Integer id) {
        boolean b = removeById(id);
        if(b){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.DELETE_ERROR.getCode(),ErrorCode.DELETE_ERROR.getMsg());
        }
    }

    public <T> List<CollectionVo<T>> copyList(List<MyCollection> myCollectionList,T t){
        List<CollectionVo<T>> collectionVos=new ArrayList<>();
        for (MyCollection myCollection : myCollectionList) {
               collectionVos.add(copy(myCollection,t));
        }
        return collectionVos;
    }
    public <T> CollectionVo copy(MyCollection myCollection,T t){
        CollectionVo collectionVo;
            if(t==Scenic.class){
                collectionVo=new CollectionVo<Scenic>();
                Scenic scenic = scenicService.selectById(myCollection.getProductId());
                collectionVo.setT(scenic);
                return collectionVo;
            }
            if(t==Route.class){
                collectionVo=new CollectionVo<Route>();
                Route route = routeService.selectByIdRoute(myCollection.getProductId());
                collectionVo.setT(route);
                return collectionVo;
            }
            return null;
    }
}
