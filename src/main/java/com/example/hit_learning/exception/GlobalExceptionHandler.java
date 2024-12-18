package com.example.hit_learning.exception;

import com.example.hit_learning.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(AppException appException){
        ErrorCode e=appException.getErrorCode();
        return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.error(e.getCode(), e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse haha(MethodArgumentNotValidException e){
        return ApiResponse.error(400,"Vui lòng điền đầy đủ thông tin cần thiết") ;
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse exception(Exception exception){
        log.error(exception.getMessage());
        //ErrorCode e=ErrorCode.INTERNAL_ERROR;
        return ApiResponse.error(400, exception.getMessage());
    }
}
