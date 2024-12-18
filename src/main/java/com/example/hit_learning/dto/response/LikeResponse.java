package com.example.hit_learning.dto.response;

import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private int id;
    private User user;
    private Item item;

}
