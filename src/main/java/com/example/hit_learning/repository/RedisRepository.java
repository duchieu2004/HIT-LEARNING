package com.example.hit_learning.repository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RedisRepository {
    void set(String key, Object value) ;
    Object get(String key) ;
    void setHashRedis(String key,  String field, Object value) ;
    Object getHash(String key, String field) ;
    List<Object> getAll(String key) ;
    void delete(String key) ;
    String convertToJson(Object t) ;
    Object convertToObject(String json, Class t) ;
    void deleteAll(String key) ;
    void delete(String key, String field) ;
    void setTTL(String key, Long timeToLive);
}

