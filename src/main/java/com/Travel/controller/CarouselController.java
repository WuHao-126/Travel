package com.Travel.controller;

import com.Travel.dao.pojo.Carousel;
import com.Travel.server.CarouselService;
import com.Travel.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("carousel")
public class CarouselController {
    @Autowired
    private CarouselService carouselService;
    @GetMapping
    public Result selectAllCarousel(){
        return carouselService.selectAllCarousel();
    }
    @PutMapping
    public Result updateCarousel(@RequestBody Carousel carousel){
        return carouselService.updateCarousel(carousel);
    }
}
