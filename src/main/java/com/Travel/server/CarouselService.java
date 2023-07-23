package com.Travel.server;

import com.Travel.dao.pojo.Carousel;
import com.Travel.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CarouselService extends IService<Carousel> {
    /**
     * 搜索所有轮播图
     * @return
     */
     Result selectAllCarousel();

    /**
     * 修改轮播图
     * @param carousel
     * @return
     */
     Result updateCarousel(Carousel carousel);
}
