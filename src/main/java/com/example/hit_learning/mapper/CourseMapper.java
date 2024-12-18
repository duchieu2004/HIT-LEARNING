package com.example.hit_learning.mapper;

import com.example.hit_learning.dto.request.CourseRequest;
import com.example.hit_learning.dto.response.CourseResponse;
import com.example.hit_learning.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRequest request) ;
    void updCourse(@MappingTarget Course course, CourseRequest request) ;
    @Mapping(target = "leaderName" , source = "user.name")
    @Mapping(target = "leaderId", source = "user.id")
    CourseResponse toResponse(Course course) ;
}
