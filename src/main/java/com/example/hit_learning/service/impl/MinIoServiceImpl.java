package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.response.FileResponse;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.service.MinIoService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.hit_learning.constant.ApplicationConstants.*;
import static com.example.hit_learning.constant.ApplicationConstants.BYTES;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinIoServiceImpl implements MinIoService {

    final MinioClient minioClient ;
    final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public StreamingResponseBody getStreamVideo(InputStream inputStream, long fileSize) {
        return outputStream -> {
            try {
                byte[] buffer = new byte[1024];
                Future<?> future = executorService.submit(() -> {
                    int bytesRead;
                    long bytesToRead = fileSize;
                    try {
                        while (bytesToRead > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, bytesToRead))) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                            outputStream.flush();
                            bytesToRead -= bytesRead;
                        }
                    } catch (IOException e) {

                    }
                });

                try {
                    future.get();
                } catch (Exception e) {
                    // Xử lý ngoại lệ nếu cần
                }
            } finally {
                inputStream.close();
            }
        };
    }


    @Override
    public InputStreamResource getVideo(String fileName){
        FileResponse fileResponse= getFile(fileName);
        return new InputStreamResource(fileResponse.getStream());
    }

    @Override
    public ResponseEntity<byte[]> prepareContent(String fileName, String range) {
        try {
            FileResponse fileResponse = getFile(fileName) ;
            long rangeStart = 0;
            long rangeEnd = CHUNK_SIZE;
            final Long fileSize =  fileResponse.getFileSize();
            String contentLength ;

            if(fileResponse.getContentType().equals("image/jpeg")){
                rangeEnd = fileResponse.getFileSize() ;
                contentLength=String.valueOf(rangeEnd) ;
            }
            else {
                if (range == null) {
                    rangeEnd = Math.min(rangeEnd, fileSize);
                    contentLength=String.valueOf(fileSize);
                } else {
                    String[] ranges = range.split("-");
                    rangeStart = Long.parseLong(ranges[0].substring(6));
                    if (ranges.length > 1) {
                        rangeEnd = Long.parseLong(ranges[1]);
                    } else {
                        rangeEnd = rangeStart + CHUNK_SIZE;
                    }
                    rangeEnd = Math.min(rangeEnd, fileSize - 1);
                    contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
                }
            }
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.parseMediaType(fileResponse.getContentType()))
                    .header(ACCEPT_RANGES, BYTES)
                    .header(CONTENT_LENGTH, contentLength)
                    .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                    .body(readByteRangeNew(fileResponse, rangeStart , rangeEnd));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public FileResponse getFile(String fileName) {
        try {
            var data= minioClient.statObject(StatObjectArgs.builder() // lay ra thong tin file da luu
                    .bucket(BUCKET)
                    .object(fileName)
                    .build()) ;
            var x= minioClient.getObject(GetObjectArgs.builder() // lay ra du lieu cua file da luu
                    .bucket(BUCKET)
                    .object(fileName)
                    .build()) ;
            return  FileResponse.builder()
                    .filename(data.object())
                    .contentType(data.contentType())
                    .fileSize(data.size())
                    .stream(x)
                    .build() ;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new AppException(ErrorCode.MINIO_NOT_FOUND) ;
        }
    }

    private byte[] readByteRangeNew(FileResponse fileResponse, long start, long end) throws IOException {
        InputStream inputStream = fileResponse.getStream();

        System.out.println(start + " " + end +"/ " + fileResponse.getFileSize());

        int size=(int)(end-start+1);

        inputStream.skip(start);

        if(size > Math.pow(10, 8)){
            size = (int)Math.pow(10, 8);
        }

        byte[] result= inputStream.readNBytes(size);
        //inputStream.readNBytes(result, (int)start , size);
        //System.arraycopy(data, (int)start, result, 0 , size);
        return result;
    }
}