package com.Travel.server.impl;

import com.Travel.dao.mapper.InformationMapper;
import com.Travel.dao.mapper.ScenicMapper;
import com.Travel.dao.pojo.Information;
import com.Travel.dao.pojo.Route;
import com.Travel.dao.pojo.Scenic;
import com.Travel.server.InformationService;
import com.Travel.server.ScenicService;
import com.Travel.vo.ErrorCode;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationServiceimpl extends ServiceImpl<InformationMapper, Information> implements InformationService {
    @Autowired
    private InformationMapper informationMapper;

    @Override
    public Result addInformation(Information information) {
        information.setViews(0);
        boolean save = save(information);
        if(save){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
    }

    @Override
    public Result selectAllInformation(PageParam pageParam) {
        Page<Information> page = query().page(new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Information> list = page.getRecords();
        if(list!=null){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result deleteInformation(Integer id) {
        boolean flag = removeById(id);
        if(flag){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.DELETE_ERROR.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result updateInformation(Information information) {
        Integer id = information.getId();
        if(id==null){
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_EMPTY.getMsg());
        }
        System.out.println(information.getDetailedText_html());
        boolean flag = updateById(information);
        if(flag){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
    }

    @Override
    public Result selectByIdInformation(Integer id) {
        Information information = informationMapper.selectById(id);
        if(information!=null){
            return Result.success(information);
        }
        return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
    }

    @Override
    public Result selectHotInformation() {
        Page<Information> page = query().orderByAsc("sort").page(new Page<Information>(1, 8));
        List<Information> list = page.getRecords();
        if(!list.isEmpty()){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectWithinInformation(PageParam pageParam) {
        Page<Information> page = query().eq("type","站内新闻").page(new Page<Information>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Information> records = page.getRecords();
        if(records!=null){
            return Result.success(records);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }
    @Override
    public Result selectOutsideInformation(PageParam pageParam) {
        Page<Information> page = query().eq("type","站外新闻").page(new Page<Information>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Information> records = page.getRecords();
        if(records!=null){
            return Result.success(records);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectTitle(String title) {
        if("".equals(title) || title==null){
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_EMPTY.getMsg());
        }
        List<Information> list = query().eq("title", title).list();
        if(list!=null){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }
}
