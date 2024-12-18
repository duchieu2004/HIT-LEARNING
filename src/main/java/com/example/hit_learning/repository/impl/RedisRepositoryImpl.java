package com.example.hit_learning.repository.impl;

import com.example.hit_learning.repository.RedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    final RedisTemplate<String, Object> redisTemplate ;
    final ObjectMapper objectMapper ;
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setHashRedis(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public Object getHash(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public List<Object> getAll(String key) {
        return redisTemplate.opsForHash().values(key) ;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key) ;
    }

    @SneakyThrows
    @Override
    public String convertToJson(Object t) {
        return objectMapper.writer().writeValueAsString(t) ;
    }
    @Override
    @SneakyThrows
    public Object convertToObject(String json, Class t ) {
        return objectMapper.readValue(json ,  t);
    }

    @Override
    public void deleteAll(String key){
        redisTemplate.delete(key) ;
    }

    @Override
    public void delete(String key, String field) {
        redisTemplate.opsForHash().keys("*") ;
    }

    @Override
    public void setTTL(String key, Long timeToLive) {
        redisTemplate.expire(key, timeToLive, TimeUnit.MILLISECONDS);
    }
}
