package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.CourseRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.dto.response.CourseResponse;
import com.example.hit_learning.dto.response.FileResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.entity.Course;
import io.minio.GetObjectResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public interface CourseService {
    CourseResponse addCourse(CourseRequest request) throws IOException; // Thêm mới một khoá học
    CourseResponse updCourse(String courseId , CourseRequest request) ; // Cập nhập một khoá học
    List<CourseResponse> delCourse(String... coursesId) ; // Xoá một danh sách khoá học
    Course findCourseById(String courseId) ; // tìm kiếm khoá học
    void segmentVideo(InputStream inputStream, HttpServletResponse response, int segmentStart, int segmentDuration) throws IOException, InterruptedException ;
    PageResponse<CourseResponse> findAll(PageRequest request) ;
    List<CourseResponse> getAllCourseByUser(String userId);
//    int getTotalStudent(String courseId) ; // bo sung sau
//    long getAllView(String courserId) ; // bo sung sau khi xong API noi dung bai hoc
}
