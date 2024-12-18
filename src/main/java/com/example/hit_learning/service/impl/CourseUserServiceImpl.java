package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.CourseUserResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.dto.response.UserResponse;
import com.example.hit_learning.entity.Course;
import com.example.hit_learning.entity.CourseUser;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.entity.compositeKey.UserCourseKey;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.mapper.UserMapper;
import com.example.hit_learning.repository.CourseRepository;
import com.example.hit_learning.repository.CourseUserRepository;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.CourseUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseUserServiceImpl implements CourseUserService {

    final String STUDENT_EXITED= "Người này đã có trong khoá học" ;
    final String SUCCESSION= "Thêm thành công" ;
    final String KEY="course-user";
    final UserRepository userRepository ;
    final CourseRepository courseRepository ;
    final CourseUserRepository courseUserRepository ;
    final RedisRepository redisRepository;
    final UserMapper userMapper;

    @Override
    public List<CourseUserResponse> addUserToCourse(List<String> userIds, String courseId) {
        Course course = getCourse(courseId);
        List<CourseUserResponse> responses = new ArrayList<>() ;
        for(String x : userIds){
            Optional<User> userOptional= userRepository.findById(x);

            if(userOptional.isPresent()){
                UserCourseKey userCourseKey = new UserCourseKey() ;
                userCourseKey.setUserId(x);
                userCourseKey.setCourseId(courseId);

                Optional<CourseUser> courseUserOptional = courseUserRepository.findById(userCourseKey) ;
                if(courseUserOptional.isEmpty()) {
                    CourseUser courseUser = new CourseUser() ;
                    courseUser.setId(userCourseKey);
                    courseUser.setUser(userOptional.get());
                    courseUser.setCourse(course);
                    courseUserRepository.save(courseUser);
                }

                CourseUserResponse courseUserResponse = new CourseUserResponse() ;
                courseUserResponse.setCourseName(course.getName());
                courseUserResponse.setFullName(userOptional.get().getName());
                courseUserResponse.setStatus(SUCCESSION);
                if(courseUserOptional.isPresent()){
                    courseUserResponse.setStatus(STUDENT_EXITED);
                }

                responses.add(courseUserResponse) ;
            }
        }
        redisRepository.deleteAll(KEY);
        return responses;
    }

    private Course getCourse(String courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(
                ()  -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
        );
        return  course ;
    }

    @Override
    public List<CourseUserResponse> removeUserFromCourse(List<String> userIds, String courseId) {
        List<CourseUserResponse> responses = new ArrayList<>() ;
        for(String x : userIds){

            UserCourseKey userCourseKey = new UserCourseKey() ;
            userCourseKey.setUserId(x);
            userCourseKey.setCourseId(courseId);

            Optional<CourseUser> courseUserOptional = courseUserRepository.findById(userCourseKey)  ;

            if(courseUserOptional.isPresent()){
                courseUserRepository.delete(courseUserOptional.get());

                CourseUserResponse courseUserResponse = new CourseUserResponse() ;
                courseUserResponse.setCourseName(courseUserOptional.get().getCourse().getName());
                courseUserResponse.setFullName(courseUserOptional.get().getUser().getName());

                responses.add(courseUserResponse) ;
            }
        }
        redisRepository.deleteAll(KEY);
        return responses;
    }

    @Override
    public PageResponse<UserResponse> getAllUserByCourse(String courseId, PageRequest request) {
        final String FIELD = ": " + courseId + request.toString();
        var json=redisRepository.getHash(KEY, FIELD);
        if(json == null) {

            Pageable pageable = request.getPageable();
            Page<CourseUser> page = courseUserRepository.findAllByCourse(courseId, pageable);
            List<UserResponse> userResponses =new ArrayList<>();
            for(CourseUser courseUser: page.getContent()){
                var x= userMapper.toResponse(courseUser.getUser());
                userResponses.add(x);
            }
            PageResponse<UserResponse> response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders(), userResponses, page.getTotalPages());
            redisRepository.setHashRedis(KEY, FIELD, redisRepository.convertToJson(response));
            return response;
        }
        PageResponse<UserResponse> response= (PageResponse<UserResponse>) redisRepository.convertToObject(json+"", PageResponse.class);
        return response;
    }
}
