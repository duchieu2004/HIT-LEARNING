package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.CourseRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.dto.response.CourseResponse;
import com.example.hit_learning.dto.response.FileResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.entity.Course;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(value = "/course")
@RequiredArgsConstructor
@Tag(name = "Api COURSE" , description = "Phần videoId dùng cho truy vấn trong Api stream video, videoName là tên thư mục người dùng up lên")
public class CourseController {

    private final CourseService courseService ;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse addCourse(@ModelAttribute @Valid CourseRequest request) throws IOException {
        CourseResponse response = courseService.addCourse(request) ;
        if(response == null) return ApiResponse.error(400, "File not null") ;
        return ApiResponse.success(response) ;
    }

    @PutMapping(value = "/{courseId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse updCourse(@PathVariable String courseId, @ModelAttribute @Valid CourseRequest request){
        CourseResponse response = courseService.updCourse(courseId, request) ;
        return ApiResponse.success(response) ;
    }

    @DeleteMapping(value = "/{courseIds}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse delCourse(@PathVariable String... courseIds){
        List<CourseResponse> responses = courseService.delCourse(courseIds) ;
        return ApiResponse.success(responses) ;
    }

    @Operation(summary = "FIND COURSE BY ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "1002" , description = "Không tìm thấy khoá học")
    })
    @GetMapping(value = "/{courseId}")
    public ApiResponse findCourseById(@PathVariable String courseId) {
        Course response = courseService.findCourseById(courseId);
        return ApiResponse.success(response);
    }


    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse findAllCourseByUser(@PathVariable String userId){
        List<CourseResponse> responses = courseService.getAllCourseByUser(userId);
        return ApiResponse.success(responses);
    }

    @GetMapping()
    public ApiResponse findAllCourse(@RequestBody(required = false) PageRequest request){
        if(request == null){
            request = new PageRequest() ;
        }
        var response = courseService.findAll(request) ;
        return  ApiResponse.success(response) ;
    }
}
