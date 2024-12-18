package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.CommentRequest;
import com.example.hit_learning.dto.response.CommentResponse;
import org.springframework.stereotype.Component;

import java.util.List;


public interface CommentService {
    CommentResponse addComment(String itemId, CommentRequest commentRequest);

    List<CommentResponse> getAllComments(String itemId);

    CommentResponse deleteComment(String CommentId);
}
