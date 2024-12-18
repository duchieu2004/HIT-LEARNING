package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {
    List<CourseResponse> findCourseByName(String courseName);
    List<SectionResponse> findSectionByName(String sectionName);
    List<ItemResponse> findIemByName(String itemName);
    <T> List<String> highLightByTerm(Class<T> entity, String terms);

    PageResponse<UserResponse> filterUserByName(String name, PageRequest request);
    PageResponse<CourseResponse> filterCourseByName(String name, PageRequest request);
}
