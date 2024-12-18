package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.AddUserRequest;
import com.example.hit_learning.dto.request.ChangePasswordRequest;
import com.example.hit_learning.dto.request.UpdateUserRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.dto.response.UserResponse;
import com.example.hit_learning.entity.ERole;
import com.example.hit_learning.entity.Role;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisRepository redisRepository;
    private final AuthenticationManager authenticationManager;
    private final String KEY="user";

    @Override
    public boolean usernameExists(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean emailExists(String email){
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public ApiResponse addUser(AddUserRequest addUserRequest) {
        if (usernameExists(addUserRequest.getUsername())) {
            return ApiResponse.error(400, String.format("Username %s already exists", addUserRequest.getUsername()));
        }
        else if (emailExists(addUserRequest.getEmail())) {
            return ApiResponse.error(400, String.format("Email %s already exists", addUserRequest.getEmail()));
        }
        User user = new User();
        modelMapper.map(addUserRequest,user);
        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.USER));
        user.setRoles(roles);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        modelMapper.map(user,userResponse);
        deleteRedis();
        return ApiResponse.success(userResponse);
    }

    @Override
    public ApiResponse getALlUser(){
        final String field="get-all";
        Object jsonValue= redisRepository.getHash(KEY, field);
        if(jsonValue == null) {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return ApiResponse.error(400, "No users found");
            }
            ApiResponse response = ApiResponse.success(users);
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        }else{
            ApiResponse response = (ApiResponse) redisRepository.convertToObject(jsonValue+"", ApiResponse.class);
            return  response;
        }
    }

    @Override
    public ApiResponse findUserById(String id){
        String field="id: " + id;
        Object jsonValue= redisRepository.getHash(KEY, field);
        if(jsonValue == null) {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_EXISTED)
            );
            UserResponse userResponse = new UserResponse();
            modelMapper.map(user, userResponse);
            ApiResponse response= ApiResponse.success(userResponse);
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        }else{
            ApiResponse response= (ApiResponse) redisRepository.convertToObject(jsonValue+"", ApiResponse.class);
            return response;
        }
    }

    @Override
    public ApiResponse updateUserById(UpdateUserRequest updateUserRequest, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        modelMapper.map(updateUserRequest,user);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        modelMapper.map(user,userResponse);
        deleteRedis();
        return ApiResponse.success(userResponse);
    }

    @Override
    public ApiResponse deleteUserById(String id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        userRepository.delete(user);
        UserResponse userResponse = new UserResponse();
        modelMapper.map(user, userResponse);
        deleteRedis();
        return ApiResponse.success(userResponse);
    }

    private void deleteRedis(){
        redisRepository.deleteAll(KEY);
    }

    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        User user= userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        if(request.getNewPassword().equals(request.getConfirmPassword())) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.getOldPassword()));
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false ;
    }

    @Override
    public boolean resetPassword(ChangePasswordRequest request) {
        User user= userRepository.findUserByUsername(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        if(!request.getNewPassword().equals(request.getConfirmPassword())) return false;
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }
}
