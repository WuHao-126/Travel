package com.Travel.controller;

import com.Travel.dao.pojo.Information;
import com.Travel.dao.pojo.Scenic;
import com.Travel.server.InformationService;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("/information")
public class InformationController {
    @Autowired
    private InformationService informationService;

    /**
     * TODO 添加资讯
     * @param information
     * @return
     */
    @PostMapping()
    public Result addInformation(@RequestBody Information information){
        return informationService.addInformation(information);
    }

    /**
     * TODO 搜索所有资讯并分页
     * @param pageParam
     * @return
     */
    @PostMapping("/page")
    public Result selectAllInformation(@RequestBody PageParam pageParam){
        return informationService.selectAllInformation(pageParam);
    }

    /**
     * TODO 站内资讯
     * @param pageParam
     * @return
     */
    @PostMapping("/within")
    public Result selectWithinInformation(@RequestBody PageParam pageParam){
        return informationService.selectWithinInformation(pageParam);
    }

    /**
     * TODO 站外资讯
     * @param pageParam
     * @return
     */
    @PostMapping("/outside")
    public Result selectOutsideInformation(@RequestBody PageParam pageParam){
        return informationService.selectOutsideInformation(pageParam);
    }

    /**
     * TODO 根据ID查询资讯
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result selectByIdInformation(@PathVariable("id") Integer id){
        return informationService.selectByIdInformation(id);
    }

    /**
     * TODO 根据标题查询
     * @param title
     * @return
     */
    @GetMapping("/search/{title}")
    public Result selectTitle(@PathVariable("title") String title){
        return informationService.selectTitle(title);
    }

    /**
     * TODO 热点资讯
     * @return
     */
    @GetMapping("/hot")
    public Result selectHotInformation(){
        return informationService.selectHotInformation();
    }
    /**
     * TODO 删除资讯
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteInformation(@PathVariable("id") Integer id){
        return informationService.deleteInformation(id);
    }

    /**
     * TODO 修改资讯
     * @param information
     * @return
     */
    @PutMapping
    public Result updateInformation(@RequestBody Information information){
        return informationService.updateInformation(information);
    }
}
