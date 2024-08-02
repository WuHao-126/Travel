package com.Travel.controller;

import com.Travel.server.MyCollectionService;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.CollectionParam;
import com.Travel.vo.param.common.PageParam;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api
@RequestMapping("/collection")
public class MyCollectionController {
    @Autowired
    private MyCollectionService myCollectionService;
    @PostMapping("/scenic")
    public Result selectScenicList(@RequestBody PageParam pageParam, HttpServletRequest servletRequest){
        return myCollectionService.selectScenicList(pageParam,servletRequest);
    }
    @PostMapping("/route")
    public Result selectRouteList(@RequestBody PageParam pageParam,HttpServletRequest servletRequest) {
        return myCollectionService.selectRouteList(pageParam,servletRequest);
    }
    @PostMapping()
    public Result addCollection(@RequestBody CollectionParam collectionParam,HttpServletRequest servletRequest){
        return myCollectionService.addCollection(collectionParam,servletRequest);
    }
    @Delete("/{id}")
    public Result deleteCollection(@PathVariable("id") Integer id){
        return  myCollectionService.deleteCollection(id);
    }

    @PostMapping("/colsceinc")
    public Result getUserCollectionScenic(@RequestBody CollectionParam collectionParam){
        return myCollectionService.getUserCollectionScenic(collectionParam);
    }
    @PostMapping("/colrouter")
    public Result getUserCollectionRouter(@RequestBody CollectionParam collectionParam){
        return myCollectionService.getUserCollectionRouter(collectionParam);
    }


    @PostMapping("/blog")
    public Result selectBlogList(@RequestBody PageParam pageParam,HttpServletRequest servletRequest){
        return myCollectionService.selectBlogList(pageParam,servletRequest);
    }
}
