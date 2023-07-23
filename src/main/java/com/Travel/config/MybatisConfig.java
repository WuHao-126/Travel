package com.Travel.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.Travel.dao.mapper")
public class MybatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
          MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();
          mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
          return mybatisPlusInterceptor;
    }
}
