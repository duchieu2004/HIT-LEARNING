package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.request.SectionRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/section")
@RequiredArgsConstructor
@Tag(name = "API Section" , description = "Thư mục con bên trong khoá học")
public class SectionController {

    final SectionService sectionService  ;

    @RequestMapping(method = RequestMethod.POST)
    @Operation(summary = "ADD Section", description = "Thêm thư mục con bên trong thư mục khoá học, bao quát các nội dung nhỏ chứa video bài giảng, nhớ kèm theo vị trí của section này muốn đặt ở đâu")
    @ApiResponses(@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode= "1002", description = "Không tìm thấy khoá học"))
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse addSection(@RequestBody @Valid SectionRequest request){
        var response= sectionService.addSection(request) ;
        return ApiResponse.success(response) ;
    }


    @PutMapping(value = "/{sectionId}")
    @Operation(summary = "UPDATE Section", description = "Cập nhập thư mục con bên trong thư mục khoá học, bao quát các nội dung nhỏ chứa video bài giảng, nhớ kèm theo vị trí của section này muốn đặt ở đâu")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse updSection(@PathVariable String sectionId,
                                  @RequestBody @Valid SectionRequest request){
        var response = sectionService.updSection(sectionId, request) ;
        return  ApiResponse.success(response) ;
    }

    @DeleteMapping(value = "/{sectionIds}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse delSection(@PathVariable String... sectionIds){
        var response = sectionService.delSections(sectionIds) ;
        return ApiResponse.success(response) ;
    }


    @GetMapping(value = "/{sectionId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse findById(@PathVariable String sectionId){
        var response = sectionService.findById(sectionId) ;
        return ApiResponse.success(response) ;
    }


    @GetMapping
    @Operation(summary = "Tìm kiếm toàn bộ section của tất cả các khoá học")
    public ApiResponse findAll(@RequestBody(required = false) PageRequest request){
        if(request == null){
            request = new PageRequest() ;
        }
        var response = sectionService.findAll(request) ;
        return ApiResponse.success(response) ;
    }

    @GetMapping(value = "course/{courseId}")
    @Operation(summary = "Danh sách tất cả các section trong Course")
    public ApiResponse findAllByCourse(@PathVariable String courseId , @RequestBody(required = false) PageRequest request){
        if(request == null){
            request = new PageRequest() ;
        }
        var response = sectionService.findAllByCourse(courseId, request);
        return ApiResponse.success(response) ;
    }




}
