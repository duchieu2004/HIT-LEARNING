package com.example.hit_learning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port ;
    @Value("${spring.data.redis.host}")
    private String host ;

    @Value("${spring.data.redis.password}")
    private String password ;

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory (){
        RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
        configuration.setPort(port);
        configuration.setHostName(host);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration) ;
    }

    @Bean
    RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate<>() ;
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        return  redisTemplate ;
    }





}
