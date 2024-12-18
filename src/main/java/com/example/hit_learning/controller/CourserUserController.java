package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.CourseUserRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.service.CourseUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class CourserUserController {
    final CourseUserService courseUserService ;

    @PostMapping
    public ApiResponse addStudent(@RequestBody CourseUserRequest request){
        var response = courseUserService.addUserToCourse(request.getUserIds() , request.getCourseId()) ;
        return ApiResponse.success(response) ;
    }

    @DeleteMapping
    public ApiResponse delStudent(@RequestBody CourseUserRequest request){
        var response = courseUserService.removeUserFromCourse(request.getUserIds() , request.getCourseId()) ;
        return ApiResponse.success(response) ;
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Tìm kiếm tất cả sinh viên theo khoá học")
    public ApiResponse getAllStudentByCourse(@PathVariable String courseId,
                                             @RequestBody(required = false) PageRequest request){
        if(request == null) request = new PageRequest();
        var response= courseUserService.getAllUserByCourse(courseId, request);
        return ApiResponse.success(response);
    }
}
