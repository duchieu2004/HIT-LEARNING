package com.example.hit_learning.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String password;
    private String name;
    @Email(message = "Email invalid")
    private String email;
    private String linkFb;
    private String description;
    private String linkAvatar;
    private String className;
}
