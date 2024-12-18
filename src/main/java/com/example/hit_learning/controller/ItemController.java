package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.ItemRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.dto.response.ItemResponse;
import com.example.hit_learning.service.ItemService;
import com.example.hit_learning.repository.RedisRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {

    final ItemService itemService ;
    final RedisRepository redisRepository;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse addItem(@ModelAttribute @Valid ItemRequest request){
        var response = itemService.addItem(request) ;
        return ApiResponse.success(response) ;
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse updItem(@PathVariable String itemId,@ModelAttribute @Valid ItemRequest request ){
        var response = itemService.updItem(itemId, request) ;
        return ApiResponse.success(response) ;
    }

    @DeleteMapping("/{itemIds}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse delItems(@PathVariable  String... itemIds){
        var response = itemService.delItem(itemIds) ;
        return ApiResponse.success(response) ;
    }

    @GetMapping("/{itemId}")
    public ApiResponse findById(@PathVariable String itemId){
        var response = itemService.findById(itemId);
        if(response == null) return  ApiResponse.error(404 , "Bài giảng không tồn tại") ;
        return ApiResponse.success(response) ;
    }

    @GetMapping()
    @Operation(summary = "Danh sách tất cả các item")
    public ApiResponse findAll(@RequestBody(required = false) PageRequest request){
        if(request == null){
            request = new PageRequest() ;
        }
        var response = itemService.findAll(request) ;
        return ApiResponse.success(response) ;
    }

    @Operation(summary = "Tìm kiếm t1ất cả các item thuộc section")
    @GetMapping(value = "/section/{sectionId}")
    public ApiResponse findAll(@PathVariable String sectionId ,@RequestBody(required = false) PageRequest request){
        if(request == null){
            request = new PageRequest() ;
        }
        var response = itemService.findBySection(sectionId, request);
        return ApiResponse.success(response) ;
    }

}
