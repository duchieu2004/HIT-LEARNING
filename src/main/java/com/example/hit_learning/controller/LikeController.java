package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.LikeRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.entity.LikeVideo;
import com.example.hit_learning.repository.LikeVideoRepository;
import com.example.hit_learning.service.LikeVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeVideoService likeVideoService;

    @PostMapping("/{videoId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse likeVideo(@PathVariable("videoId") String videoId, @RequestBody LikeRequest likeRequest) {
        return ApiResponse.success(likeVideoService.addLike(videoId, likeRequest));

    }

    @GetMapping("/{videoId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse getLikeVideo(@PathVariable("videoId") String videoId) {
        return ApiResponse.success(likeVideoService.getAllLike(videoId));
    }

    @DeleteMapping("/{videoId}")
    @PreAuthorize("hasAnyAuthority('LEADER', 'ADMIN')")
    public ApiResponse deleteLikeVideo(@PathVariable("videoId") String videoId, @RequestBody LikeRequest likeRequest) {
        return ApiResponse.success(likeVideoService.deleteLike(videoId,likeRequest));
    }
}
