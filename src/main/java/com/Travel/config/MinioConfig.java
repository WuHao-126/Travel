package com.Travel.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private  String url;
    @Value("${minio.account}")
    private  String account;
    @Value("${minio.password}")
    private  String password;

    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient=MinioClient
                .builder()
                .endpoint(url)
                .credentials(account,password)
                .build();
        return minioClient;
    }
}
