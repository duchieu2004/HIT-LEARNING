package com.example.hit_learning.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Value("${minio.endpoint}")
     String END_POINT ;
    @Value("${minio.accesskey}")
     String ACCESS_KEY ;
    @Value("${minio.secretkey}")
    String SECRET_KEY ;
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(END_POINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }
}
