package com.Travel.controller;

import com.Travel.dao.pojo.Scenic;
import com.Travel.server.ScenicService;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import com.Travel.vo.param.ScenicParam;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api
@RequestMapping("/scenic")
public class ScenicController {
    @Autowired
    private ScenicService scenicService;

    /**
     * TODO 添加景区信息
     * @return
     */
    @PostMapping
    public Result addScenic(@RequestBody Scenic scenic){
        return scenicService.addScennic(scenic);
    }

    /**
     * TODO 搜索所有景区（分页）
     * @param
     * @return
     */
    @PostMapping("/page")
    public Result selectAllScenic(@RequestBody PageParam pageParam){
        return scenicService.selectAllScenic(pageParam);
    }

    /**
     * TODO 根据ID查询景区
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Scenic selectById(@PathVariable("id") Integer id){
        return scenicService.selectById(id);
    }
    @GetMapping("search/{sname}")
    public Result selectSName(@PathVariable("sname") String sname){
       return scenicService.selectSName(sname);
    }
    /**
     * TODO 查询热点景区
     * @return
     */
    @GetMapping("/hot")
    public Result selectHotScenic(){
        return scenicService.selectHotScenic();
    }

    /**
     * TODO 根据ID查询景区
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteScenic(@PathVariable("id") Integer id){
        return scenicService.deleteScenic(id);
    }

    /**
     * TODO 修改景区
     * @param scenic
     * @return
     */
    @PutMapping
    public Result updateScenic(@RequestBody Scenic scenic){
        return scenicService.updateScenic(scenic);
    }

    @PostMapping("/regio/{scenicName}")
    public Result selectRegio(@PathVariable("scenicName") String scenicName,@RequestBody PageParam pageParam){
        return scenicService.selectRegio(scenicName,pageParam);
    }
    @PostMapping("scenicName")
    public Result selectScenicName(@RequestBody ScenicParam scenicParam){
        return scenicService.selectScenicName(scenicParam);
    }

    
}
