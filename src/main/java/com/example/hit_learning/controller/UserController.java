package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.AddUserRequest;
import com.example.hit_learning.dto.request.ChangePasswordRequest;
import com.example.hit_learning.dto.request.UpdateUserRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    final String TAG="user";

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody AddUserRequest addUserRequest) {
        ApiResponse apiResponse = userService.addUser(addUserRequest);
        log.info(apiResponse.toString());
        if(apiResponse.getCode()==400)
        {
            return ResponseEntity.status(400).body(apiResponse);
        }
        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        ApiResponse apiResponse = userService.getALlUser();
        if (apiResponse.getCode()==400)
        {
            return ResponseEntity.status(400).body(apiResponse);
        }
        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) {
        ApiResponse apiResponse = userService.findUserById(userId);
        if (apiResponse.getCode()==1002)
        {
            return ResponseEntity.status(400).body(apiResponse);
        }
        return ResponseEntity.status(201).body(apiResponse);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateUserByID(@PathVariable("userId") String userId, @RequestBody UpdateUserRequest updateUserRequest) {
        ApiResponse apiResponse = userService.updateUserById(updateUserRequest, userId);
        if (apiResponse.getCode()==1002)
        {
            return ResponseEntity.status(400).body(apiResponse);
        }
        return ResponseEntity.status(201).body(apiResponse);
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable ("userId") String userId) {
        ApiResponse apiResponse = userService.deleteUserById(userId);
        if (apiResponse.getCode()==1002)
        {
            return ResponseEntity.status(400).body(apiResponse);
        }
        return ResponseEntity.status(201).body(apiResponse);
    }
    @PutMapping("/user/password/change")
    public ApiResponse changePassword(@RequestBody ChangePasswordRequest request){
        boolean response= userService.changePassword(request);
        if(response == true){
            return ApiResponse.success(true);
        }else{
            return ApiResponse.error(400, "Kiểm tra lại thông tin bạn nhập");
        }
    }

//    public ApiResponse filterByName(String name, Page)

    @PutMapping("/user/password/reset")
    @Operation(summary = "change password" , description = "Chỗ này kh cần truyền vào mật khẩu cũ, userId truyền vào username")
    public ApiResponse resetPassword(@RequestBody ChangePasswordRequest request){
        boolean response= userService.resetPassword(request);
        if(response == true){
            return ApiResponse.success(true);
        }else{
            return ApiResponse.error(400, "Kiểm tra lại thông tin bạn nhập");
        }
    }
}
