package com.example.hit_learning.dto.response;

import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private String comment;
    private LocalDateTime createdAt;
    private User user;
    private Item item;
}
