package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.CourseRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.CourseResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.entity.Course;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.mapper.CourseMapper;
import com.example.hit_learning.repository.CourseRepository;
import com.example.hit_learning.repository.MinIORepository;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.CourseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;


@RequiredArgsConstructor
@Component
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository ;
    private final UserRepository userRepository ;
    private final CourseMapper courseMapper ;
    private final MinIORepository minIORepository ;
    private final RedisRepository redisRepository ;
    private static final String KEY = "course" ;
    private static final String TAG = "COURSE";

    @Override
    @Transactional
    @SneakyThrows
    public CourseResponse addCourse(CourseRequest request)  {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        ) ;
        if(request.getFile() == null){
            return null ;
        }
        System.out.println(request.getFile().getSize());

        if(request.getFile().getSize()/ 1000000 > 50){
            throw new AppException(ErrorCode.COURSE_FILE_LONG);
        }
        var file = request.getFile() ;
        var course = courseMapper.toCourse(request) ;
        try{
            var data = minIORepository.upload(file) ;
            course.setUser(user);
            course.setVideoId(data.get("videoId"));
            course.setVideoName(data.get("videoName"));
            course = courseRepository.save(course) ;
        }catch (Exception e){
            throw new RuntimeException("Nơi lưu trữ đang được bảo trì");
        }
        deleteRedis();
        var response = courseMapper.toResponse(course) ;
        return  response ;
    }


    @Override
    public CourseResponse updCourse(String courseId, CourseRequest request) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
        ) ;
        courseMapper.updCourse(course , request);
        if(request.getFile() != null){
            minIORepository.deleteFile(course.getVideoId());
            final MultipartFile file = request.getFile() ;
            var data = minIORepository.upload(file) ;
            course.setVideoId(data.get("videoId"));
            course.setVideoName(data.get("videoName"));
        }
        courseRepository.save(course) ;
        deleteRedis();
        var response= courseMapper.toResponse(course) ;
        return response ;
    }




    @Override
    @SneakyThrows
    public void segmentVideo(InputStream inputStream , HttpServletResponse response, int segmentStart, int segmentDuration){

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add("TEST-HIT_LEARNING.mp4");
        command.add("-ss");
        command.add(String.valueOf(segmentStart));
        command.add("-t");
        command.add(String.valueOf(segmentDuration));
        command.add("-c:v");
        command.add("copy");
        command.add("-c:a");
        command.add("copy");
        command.add("haha.mp4");

        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            Process process = builder.start();
            process.waitFor();
            System.out.println("Video cut successfully");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        };
    }



    @Override
    public List<CourseResponse> delCourse(String... coursesId) {
        List<CourseResponse> responses = new ArrayList<>() ;
        for(String x : coursesId){
            var xy= courseRepository.findById(x) ;
            if(xy.isPresent()){
                var xyz= courseMapper.toResponse(xy.get()) ;
                responses.add(xyz) ;
                xy.get().setAvailable(false);
                courseRepository.delete(xy.get());
            }
        }
        deleteRedis();
        return responses ;
    }

    @Override
    public Course findCourseById(String courseId) {
        final String FIELD = "id:" + courseId ;
        Object json= redisRepository.getHash(KEY, FIELD) ;
        if(json == null) {
            var x = courseRepository.findById(courseId).orElseThrow(
                    () -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
            );
            json = redisRepository.convertToJson(x) ;
            redisRepository.setHashRedis(KEY, FIELD, json);
            return x ;
        }
        else{
            return (Course) redisRepository.convertToObject(json+"", Course.class) ;
        }
    }

    @Override
    public PageResponse<CourseResponse> findAll(PageRequest request) {
        String field = "find-all:" + request.toString();
        Object jsonValue = redisRepository.getHash(KEY, field);
        if (jsonValue == null) {
            Pageable pageable = request.getPageable();
            Page<Course> page = courseRepository.findAll(pageable);
            List<CourseResponse> courseResponses = new ArrayList<>();
            for (Course x : page.getContent()) {
                var xy = courseMapper.toResponse(x);
                courseResponses.add(xy);
            }
            PageResponse<CourseResponse> response = new PageResponse<>(request.getPageIndex()
                    , request.getPageSize()
                    , request.getOrders()
                    , courseResponses
                    , page.getTotalPages());
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        } else {
            PageResponse<CourseResponse> response = (PageResponse<CourseResponse>) redisRepository.convertToObject(jsonValue + "", PageResponse.class);
            return response;
        }
    }


    private void deleteRedis(){
        redisRepository.deleteAll(KEY);
        redisRepository.deleteAll("section");
        redisRepository.deleteAll("item");
    }

    @Override
    public List<CourseResponse> getAllCourseByUser(String userId) {
        String field="userId: " + userId;
        Object json = redisRepository.getHash(KEY , field);
        if(json == null) {
            List<Course> courses = courseRepository.findAllCourseByUser(userId);
            List<CourseResponse> responses = new ArrayList<>();
            for (Course x : courses) {
                responses.add(courseMapper.toResponse(x));
            }
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(responses));
            return responses;
        }
        List<CourseResponse> responses = (List<CourseResponse>) redisRepository.convertToObject(json+"", List.class);
        return responses;
    }
}
