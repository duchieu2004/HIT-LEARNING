package com.example.hit_learning.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        Map<Object , Object> map = new HashMap<>() ;
        map.put("cloud_name" , "dytxoysey") ;
        map.put("api_key" ,"921877442766949") ;
        map.put("api_secret" , "CQK7pbQY7BwcxgNGwf6kzujrEjY") ;
        map.put("secure" , true) ;
        return new Cloudinary(map) ;
    }
}
