package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.AuthenticationRequest;
import com.example.hit_learning.dto.response.AuthenticationResponse;
import com.example.hit_learning.dto.response.TokenResponse;
import com.example.hit_learning.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    TokenResponse login(AuthenticationRequest authenticationRequest);
    TokenResponse refreshToken(String refreshToken);
    void sendMail(String username);
    boolean verifyCode(String username, int code);
    void logout(String token);
}
