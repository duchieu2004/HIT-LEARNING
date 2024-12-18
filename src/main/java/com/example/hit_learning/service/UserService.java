package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.AddUserRequest;
import com.example.hit_learning.dto.request.ChangePasswordRequest;
import com.example.hit_learning.dto.request.UpdateUserRequest;
import com.example.hit_learning.dto.response.ApiResponse;

public interface UserService {
    boolean usernameExists(String username);

    boolean emailExists(String email);

    ApiResponse addUser(AddUserRequest addUserRequest);

    ApiResponse getALlUser();

    ApiResponse findUserById(String id);


    ApiResponse updateUserById(UpdateUserRequest updateUserRequest, String id);


    ApiResponse deleteUserById(String id);

    boolean changePassword(ChangePasswordRequest request);
    boolean resetPassword(ChangePasswordRequest request);
}
