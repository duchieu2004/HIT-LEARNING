package com.example.hit_learning.mapper;

import com.example.hit_learning.dto.response.UserResponse;
import com.example.hit_learning.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
