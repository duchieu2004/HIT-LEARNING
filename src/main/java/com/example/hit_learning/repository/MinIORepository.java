package com.example.hit_learning.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Repository
public interface MinIORepository {

    Map<String, String> upload(MultipartFile file) ;
    void deleteFile(String videoId) ;
}
