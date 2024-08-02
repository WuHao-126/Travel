package com.Travel.server.impl;

import com.Travel.dao.mapper.ScenicMapper;
import com.Travel.dao.pojo.Scenic;
import com.Travel.server.ScenicService;
import com.Travel.vo.ErrorCode;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.scenic.ScenicParam;
import com.Travel.vo.param.scenic.ScenicQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicServiceimpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {
    @Autowired
    private ScenicMapper scenicMapper;

    @Override
    public Result addScennic(Scenic scenic) {
        scenic.setViews(0);
        if(scenic==null){
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_EMPTY.getMsg());
        }
        boolean save = save(scenic);
        if(save){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
        }
    }

    @Override
    public Result selectAllScenic(PageParam pageParam) {
        Page<Scenic> page = query().page(new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize()));
        List<Scenic> list = page.getRecords();
        if(list!=null){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Scenic selectById(Integer id) {
        Scenic scenic = scenicMapper.selectById(id);
        if(scenic!=null){
            scenicMapper.addDetailedScenicViews(scenic.getId());
            return scenic;
        }
        return null;
    }

    @Override
    public Result deleteScenic(Integer id) {
        boolean flag = removeById(id);
        if(flag){

            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.DELETE_ERROR.getCode(),ErrorCode.NO_DATE.getMsg());
        }

    }

    @Override
    public Result updateScenic(Scenic scenic) {
        Integer id = scenic.getId();
        if(id==null || id==0 || scenic==null){
             return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_EMPTY.getMsg());
        }
        boolean flag = updateById(scenic);
        if(flag){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
    }

    @Override
    public Result selectHotScenic() {
        Page<Scenic> sort = query().orderByAsc("sort").page(new Page<Scenic>(1, 8));
        List<Scenic> list = sort.getRecords();
        if(!list.isEmpty()){
            return Result.success(list);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectSName(String sname) {
        if("".equals(sname) || sname==null){
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_EMPTY.getMsg());
        }
        Scenic scenic = query().eq("sname", sname).one();
        if(scenic!=null){
            return Result.success(scenic);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }

    }

    //TODO  (待完善)
    @Override
    public Result selectRegio(String location, PageParam pageParam) {
        Page<Scenic> area = query().eq("area", location).page(new Page<Scenic>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<Scenic> records = area.getRecords();
        if(!records.isEmpty()){
             return Result.success(records);
        }else{
            return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectScenicName(ScenicParam scenicParam) {
        String area=scenicParam.getArea();
        String scenicName=scenicParam.getScenicName();
        if("".equals(area) || "".equals(scenicName)){
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        List<Scenic> list = query().eq("area", area).eq("sname", scenicName).list();
        if(!list.isEmpty()){
            return Result.success(list);
        }
        return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
    }

    @Override
    public Result searchScenic(ScenicQuery scenicQuery) {
        Integer id = scenicQuery.getId();
        String sname = scenicQuery.getSname();
        String type = scenicQuery.getType();
        String area = scenicQuery.getArea();
        String areaDetailed = scenicQuery.getAreaDetailed();
        Integer smallPrice = scenicQuery.getSmallPrice();
        Integer bigPrice = scenicQuery.getBigPrice();
        Integer level = scenicQuery.getLevel();
        Integer currentPage = scenicQuery.getCurrentPage();
        Integer pageSize = scenicQuery.getPageSize();
        QueryWrapper<Scenic> queryWrapper=new QueryWrapper<>();
        if(id!=null){
            Scenic scenic = query().eq("id", id).one();
            return Result.success(scenic);
        }
        queryWrapper.like(!StringUtils.isBlank(sname),"sname",sname);
        queryWrapper.eq(!StringUtils.isBlank(type),"type",type);
        queryWrapper.like(!StringUtils.isBlank(area),"area",area);
        queryWrapper.like(!StringUtils.isBlank(areaDetailed),"areaDetailed",areaDetailed);
        if(level!=null){
            queryWrapper.eq("leve",level);
        }
        if(smallPrice!=null && bigPrice!=null){
           queryWrapper.between("price",smallPrice,bigPrice);
        }
        List<Scenic> records = scenicMapper.selectPage(new Page<Scenic>(currentPage, pageSize), queryWrapper).getRecords();
        return Result.success(records);
    }


}
