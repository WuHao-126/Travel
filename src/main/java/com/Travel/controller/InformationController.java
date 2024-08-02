package com.Travel.controller;

import com.Travel.dao.pojo.Information;
import com.Travel.server.InformationService;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.IdParam;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.information.InformationQuery;
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
     *  添加资讯
     * @param information
     * @return
     */
    @PostMapping()
    public Result addInformation(@RequestBody Information information){
        return informationService.addInformation(information);
    }

    @PostMapping("search")
    public Result searchInformation(@RequestBody InformationQuery informationQuery){
        if(informationQuery==null){
            throw new RuntimeException("资讯搜索参数为空");
        }
        return informationService.searchInformation(informationQuery);
    }

    /**
     *  热点资讯
     * @return
     */
    @GetMapping("/hot")
    public Result selectHotInformation(){
        return informationService.selectHotInformation();
    }
    /**
     *  删除资讯
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteInformation(@PathVariable("id") Integer id){
        return informationService.deleteInformation(id);
    }

    @PostMapping("/view")
    public Result addView(@RequestBody IdParam idParam){
        Integer id = idParam.getId();
        if(id==null){
            throw new RuntimeException("资讯添加访问量参数为空");
        }
        return informationService.addView(id);
    }
    /**
     *  修改资讯
     * @param information
     * @return
     */
    @PutMapping
    public Result updateInformation(@RequestBody Information information){
        return informationService.updateInformation(information);
    }
}
