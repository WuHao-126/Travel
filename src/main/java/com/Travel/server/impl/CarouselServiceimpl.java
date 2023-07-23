package com.Travel.server.impl;

import com.Travel.dao.mapper.CarouselMapper;
import com.Travel.dao.pojo.Carousel;
import com.Travel.server.CarouselService;
import com.Travel.vo.ErrorCode;
import com.Travel.vo.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceimpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    @Override
    public Result selectAllCarousel() {
        List<Carousel> list = query().list();
        if(list!=null){
            return Result.success(list);
        }
        return Result.fail(ErrorCode.NO_DATE.getCode(),ErrorCode.NO_DATE.getMsg());
    }

    @Override
    public Result updateCarousel(Carousel carousel) {
        int i = carouselMapper.updateById(carousel);
        if(i>0){
            return Result.success(null);
        }else{
            return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
        }
    }
}
