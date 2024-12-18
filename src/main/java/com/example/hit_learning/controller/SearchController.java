package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.entity.Course;
import com.example.hit_learning.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name="API SEARCH")
public class SearchController {

    final SearchService searchService;

    @GetMapping("/course/{term}")
    @Operation(summary = "API GET COURSE BY NAME AND DESCRIPTION")
    public ApiResponse findCourseByName(@PathVariable String term){
        var response= searchService.findCourseByName(term);
        return ApiResponse.success(response);
    }

    @GetMapping("/section/{term}")
    @Operation(summary = "API GET SECTION BY NAME AND DESCRIPTION")
    public ApiResponse findSectionByName(@PathVariable String term){
        var response= searchService.findSectionByName(term);
        return ApiResponse.success(response);
    }

    @GetMapping("/item/{term}")
    @Operation(summary = "API GET ITEM BY NAME AND DESCRIPTION")
    public ApiResponse findItemByName(@PathVariable String term){
        var response= searchService.findIemByName(term);
        return ApiResponse.success(response);
    }

    @GetMapping("/suggest/{term}")
    @Operation(summary = "API SUGGESTION PHRASE")
    public ApiResponse highlight(@PathVariable String term){
        var response= searchService.highLightByTerm(Course.class, term);
        return ApiResponse.success(response);
    }

    @GetMapping("/course/filter-name/{name}")
    public ApiResponse filterCourseByName(@PathVariable String name, PageRequest request){
        var response= searchService.filterCourseByName(name, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/user/filter-name/{name}")
    public ApiResponse filterUserByName(@PathVariable String name, PageRequest request){
        var response= searchService.filterUserByName(name, request);
        return ApiResponse.success(response);
    }
}
