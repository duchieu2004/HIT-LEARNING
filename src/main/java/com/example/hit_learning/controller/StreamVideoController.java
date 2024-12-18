package com.example.hit_learning.controller;

import com.example.hit_learning.dto.response.FileResponse;
import com.example.hit_learning.service.MinIoService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
public class StreamVideoController {

    final MinIoService minIoService ;

//    @GetMapping(value = "/stream_v2/{videoId}") // xuat tu file sang byte
//    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
//                                                    @PathVariable("videoId") String videoId) {
//        return Mono.defer(() -> {
//            ResponseEntity<byte[]> data = minIoService.prepareContent(videoId, httpRangeList);
//            return Mono.just(data);
//        });
//    }

    @GetMapping(value = "/stream/{videoId}")
    @SneakyThrows
    public ResponseEntity<StreamingResponseBody> streamVideo_v2(@PathVariable("videoId") String videoId, @RequestHeader(required = false) String rangeHeader) {
        FileResponse fileResponse = minIoService.getFile(videoId);
        InputStream inputStream = fileResponse.getStream();
        Long fileSize= fileResponse.getFileSize();


        long videoSize = fileResponse.getFileSize();
        long start = 0;
        long end = videoSize - 1;
        long contentLength = end - start + 1;

        if (rangeHeader != null) {
            List<HttpRange> ranges = HttpRange.parseRanges(rangeHeader);
            if (!ranges.isEmpty()) {
                HttpRange range = ranges.get(0);
                start = range.getRangeStart(videoSize);
                end = range.getRangeEnd(videoSize);
                contentLength = end - start + 1;
                inputStream.skip(start);
            }
        }

        StreamingResponseBody body = minIoService.getStreamVideo(inputStream, fileSize);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, fileResponse.getContentType());
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        if (rangeHeader != null) {
            headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + videoSize);
            return new ResponseEntity<>(body, headers, HttpStatus.PARTIAL_CONTENT);
        } else {
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }
    }
}
