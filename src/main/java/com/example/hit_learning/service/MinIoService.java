package com.example.hit_learning.service;

import com.example.hit_learning.dto.response.FileResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface MinIoService {
    ResponseEntity<byte[]>  prepareContent(String fileName, String range) ;
    InputStreamResource getVideo(String fileName);
    FileResponse getFile(String findName);
    StreamingResponseBody getStreamVideo(InputStream inputStream, long fileSize);
}
