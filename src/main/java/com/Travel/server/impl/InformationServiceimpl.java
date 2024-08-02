package com.Travel.server.impl;

import com.Travel.dao.mapper.InformationMapper;
import com.Travel.dao.pojo.Information;
import com.Travel.server.InformationService;
import com.Travel.vo.ErrorCode;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.information.InformationQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public Result searchInformation(InformationQuery informationQuery) {
        Integer id = informationQuery.getId();
        String title = informationQuery.getTitle();
        String type = informationQuery.getType();
        Integer currentPage = informationQuery.getCurrentPage();
        Integer pageSize = informationQuery.getPageSize();
        Date startTime = informationQuery.getStartTime();
        Date endTime = informationQuery.getEndTime();
        if (id!=null){
            Information information = query().eq("id", id).one();
            return Result.success(information);
        }
        QueryWrapper<Information> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(id!=null,"id",id);
        queryWrapper.like(!StringUtils.isBlank(title),"title",title);
        queryWrapper.eq(!StringUtils.isBlank(type),"type",type);
        queryWrapper.between(startTime!=null && endTime!=null,"publicTime",startTime,endTime);
        queryWrapper.orderByDesc("publicTime");
        List<Information> records = informationMapper.selectPage(new Page<Information>(currentPage, pageSize), queryWrapper).getRecords();
        return Result.success(records);
    }

    @Override
    public Result deleteInformation(Integer id) {
        if(id==null){
            throw new RuntimeException("删除资讯参数错误id："+id);
        }
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
        boolean flag = updateById(information);
        if(flag){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
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
    public Result addView(Integer id) {
        Information information = query().eq("id", id).one();
        if(information==null){
            throw new RuntimeException("资讯不存在id:"+id);
        }
        information.setViews(information.getViews()+1);
        boolean b = updateById(information);
        if(b){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
    }

}
