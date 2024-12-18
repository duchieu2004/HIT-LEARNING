package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.CommentRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.service.CommentService;
import com.example.hit_learning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {


    @Autowired
    private CommentService commentService;
    @Autowired
    private  CourseService courseService;



    @PostMapping("/{itemId}")
    private ApiResponse addComment(@PathVariable("itemId") String itemId, @RequestBody CommentRequest commentRequest) {
        return ApiResponse.success(commentService.addComment(itemId,commentRequest));
    }

    @GetMapping("/{itemId}")
    private ApiResponse getComment(@PathVariable("itemId") String itemId) {
        return ApiResponse.success((commentService.getAllComments(itemId)));
    }

    @DeleteMapping("/{commentId}")
    private ApiResponse deleteComment(@PathVariable("commentId") String commentId) {
        return ApiResponse.success(commentService.deleteComment(commentId));
    }
}
