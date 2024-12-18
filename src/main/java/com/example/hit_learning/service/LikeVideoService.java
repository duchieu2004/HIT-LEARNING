package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.LikeRequest;
import com.example.hit_learning.dto.response.LikeResponse;

import java.util.List;

public interface LikeVideoService {
    LikeResponse addLike(String videoId, LikeRequest likeRequest);

    List<LikeResponse> getAllLike(String videoId);

    LikeResponse deleteLike(String videoId, LikeRequest likeRequest);
}
