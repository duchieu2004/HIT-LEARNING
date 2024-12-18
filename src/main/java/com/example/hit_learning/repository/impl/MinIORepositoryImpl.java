package com.example.hit_learning.repository.impl;

import com.example.hit_learning.repository.MinIORepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.hit_learning.constant.ApplicationConstants.BUCKET;

@Component
@RequiredArgsConstructor
public class MinIORepositoryImpl implements MinIORepository {

    final MinioClient minioClient ;

    @SneakyThrows
    public Map<String, String> upload(MultipartFile file) {
        InputStream is = file.getInputStream() ;
        final String extension = file.getOriginalFilename()
                .substring(
                        file.getOriginalFilename().lastIndexOf(".") + 1
                ) ;
        final String videoName = file.getOriginalFilename();
        final String videoId = UUID.randomUUID().toString() + "." + extension ;
        minioClient.putObject(PutObjectArgs.builder()
                .stream(is , is.available(), -1)
                .bucket(BUCKET)
                .contentType(file.getContentType())
                .object(videoId)
                .build()) ;
        Map<String,String> data = new HashMap<>() ;
        data.put("videoName" , videoName) ;
        data.put("videoId", videoId) ;
        return data ;
    }

    @SneakyThrows
    public void deleteFile(String videoId) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(BUCKET)
                .object(videoId)
                .build());
    }
}
