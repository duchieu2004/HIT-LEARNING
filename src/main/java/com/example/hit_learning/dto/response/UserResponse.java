package com.example.hit_learning.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String linkFb;
    private String linkAvatar;
    private String description;
    private String className;
}
