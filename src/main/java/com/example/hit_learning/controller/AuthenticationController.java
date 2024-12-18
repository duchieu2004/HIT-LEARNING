package com.example.hit_learning.controller;

import com.example.hit_learning.dto.request.AuthenticationRequest;
import com.example.hit_learning.dto.response.ApiResponse;
import com.example.hit_learning.dto.response.AuthenticationResponse;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "API LOGIN")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "LOGIN")
    public ApiResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ApiResponse.success(authenticationService.login(authenticationRequest));
    }

    @PostMapping("/logout/{token}")
    public ApiResponse logout(@PathVariable String token){
        authenticationService.logout(token);
        return ApiResponse.success("Đăng xuất thành công");
    }


    @PostMapping("/refresh/{refreshToken}")
    @Operation(summary = "REFRESH TOKEN")
    public ApiResponse refreshToken(@PathVariable String refreshToken){
        return ApiResponse.success(authenticationService.refreshToken(refreshToken));
    }

    @PostMapping("/mail/{username}")
    @Operation(summary = "SEND CODE")
    public ApiResponse sendMail(@PathVariable String username){
        System.out.println(username);
        authenticationService.sendMail(username);
        return ApiResponse.success("SEND MAIL SUCCESS");
    }

    @PostMapping("/verify/{username}/{code}")
    public ApiResponse verifyCode(@PathVariable String username,
                                  @PathVariable Integer code){
        if(authenticationService.verifyCode(username, code)){
            return ApiResponse.success("Success");
        }return ApiResponse.error(403, "CODE IS INCORRECT");
    }

}
