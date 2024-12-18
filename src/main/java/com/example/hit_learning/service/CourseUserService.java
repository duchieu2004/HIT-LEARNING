package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.CourseUserResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseUserService {
    List<CourseUserResponse> addUserToCourse(List<String> userIds, String courseId) ;
    List<CourseUserResponse> removeUserFromCourse(List<String> userIds, String courseId) ;
    PageResponse<UserResponse> getAllUserByCourse(String courseId, PageRequest request);
}
