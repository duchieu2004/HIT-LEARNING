package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.LikeRequest;
import com.example.hit_learning.dto.response.LikeResponse;
import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.LikeVideo;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.entity.compositeKey.LikeVideoKey;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.repository.ItemRepository;
import com.example.hit_learning.repository.LikeVideoRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.LikeVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeVideoServiceImpl implements LikeVideoService {
    @Autowired
    private LikeVideoRepository likeVideoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public LikeResponse addLike(String videoId, LikeRequest likeRequest) {
        User user = userRepository.findByUsername(likeRequest.getUsername());
        Item item = itemRepository.findById(videoId).orElseThrow(
                ()-> new AppException(ErrorCode.ITEM_NOT_EXISTED)
        );

        LikeVideoKey likeVideoKey = new LikeVideoKey();
        likeVideoKey.setUserId(user.getId());
        likeVideoKey.setItemId(item.getId());

        LikeVideo likeVideo = new LikeVideo();
        likeVideo.setId(likeVideoKey);
        likeVideo.setUser(user);
        likeVideo.setItem(item);
        likeVideoRepository.save(likeVideo);

        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setUser(user);
        likeResponse.setItem(item);
        return likeResponse;
    }

    @Override
    public List<LikeResponse> getAllLike(String videoId){
        Item item = itemRepository.findById(videoId).orElseThrow(
                ()-> new AppException(ErrorCode.ITEM_NOT_EXISTED)
        );

        List<LikeVideo> likeVideos = likeVideoRepository.findAllByItem(item);
        List<LikeResponse> likeResponses = new ArrayList<>();

        for(LikeVideo likeVideo : likeVideos){
            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setUser(likeVideo.getUser());
            likeResponse.setItem(likeVideo.getItem());
            likeResponses.add(likeResponse);
        }
        return likeResponses;

    }

    @Override
    public LikeResponse deleteLike (String videoId, LikeRequest likeRequest){
        User user = userRepository.findByUsername(likeRequest.getUsername());
        Item item = itemRepository.findById(videoId).orElseThrow(
                ()-> new AppException(ErrorCode.ITEM_NOT_EXISTED)
        );

        LikeVideoKey likeVideoKey = new LikeVideoKey();
        likeVideoKey.setUserId(user.getId());
        likeVideoKey.setItemId(item.getId());

        LikeVideo likeVideo = new LikeVideo();
        likeVideo.setId(likeVideoKey);
        likeVideo.setUser(user);
        likeVideo.setItem(item);
        likeVideoRepository.delete(likeVideo);

        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setUser(user);
        likeResponse.setItem(item);
        return likeResponse;


    }

}
