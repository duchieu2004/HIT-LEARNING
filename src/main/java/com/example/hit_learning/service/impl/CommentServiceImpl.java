package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.CommentRequest;
import com.example.hit_learning.dto.response.CommentResponse;
import com.example.hit_learning.entity.Comment;
import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.repository.CommentRepository;
import com.example.hit_learning.repository.ItemRepository;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {


    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final String KEY="comment";


    @Override
    public CommentResponse addComment(String itemId, CommentRequest commentRequest) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                ()->new AppException(ErrorCode.ITEM_NOT_EXISTED)
        );

        User user = userRepository.findByUsername(commentRequest.getUsername());

        Comment comment = new Comment();
        comment.setContent(commentRequest.getComment());

        user.addComment(comment);
        item.addComment(comment);

        System.out.println(comment);
        commentRepository.save(comment);

        this.deleteRedis(KEY);

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setUser(comment.getUser());
        commentResponse.setItem(comment.getItem());
        commentResponse.setComment(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setId(comment.getId());
        return commentResponse;

    }

    @Override
    public List<CommentResponse> getAllComments(String itemId) {
        final String field= "get-all: " + itemId;
        Object jsonValue= redisRepository.getHash(KEY, field);
        if(jsonValue == null) {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_EXISTED));
            List<Comment> comments = commentRepository.findAllByItem(item);
            List<CommentResponse> commentResponses = new ArrayList<>();
            for (Comment comment : comments) {
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setUser(comment.getUser());
                commentResponse.setItem(comment.getItem());
                commentResponse.setComment(comment.getContent());
                commentResponse.setCreatedAt(comment.getCreatedAt());
                commentResponse.setId(comment.getId());
                commentResponses.add(commentResponse);
            }
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(commentResponses));
            return commentResponses;
        }else{
            List<CommentResponse> responses= (List<CommentResponse>) redisRepository.convertToObject(jsonValue+"", List.class);
            return responses;
        }
    }

    @Override
    public CommentResponse deleteComment(String CommentId)
    {
        Comment comment = commentRepository.findById(CommentId).orElseThrow(
                ()->new AppException(ErrorCode.COMMENT_NOT_EXISTED)
        );
        commentRepository.delete(comment);
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setUser(comment.getUser());
        commentResponse.setItem(comment.getItem());
        commentResponse.setComment(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setId(comment.getId());
        this.deleteRedis(KEY);
        return commentResponse;

    }

    private void deleteRedis(String key){
        redisRepository.deleteAll(KEY);
    }
}

