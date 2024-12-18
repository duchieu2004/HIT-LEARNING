package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.AuthenticationRequest;
import com.example.hit_learning.dto.response.TokenResponse;
import com.example.hit_learning.entity.Role;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.AuthenticationService;
import com.example.hit_learning.service.JwtService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@RequiredArgsConstructor
@Component
@EnableAsync
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;
    final RedisRepository redisRepository;
    private final Long TIME_TOKEN=1000*60*60*24L; // 1 ngay
    private final Long TIME_REFRESH_TOKEN=100*60*60*24*2L;//2 ngay

    @Override
    public TokenResponse login(AuthenticationRequest authenticationRequest){
        User user = userRepository.findByUsername(authenticationRequest.getUsername());

        if (user == null)
        {
            throw new AppException(ErrorCode.USER_UNAUTHENTICATED);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            ));
        }catch (Exception e){
            throw  new AppException(ErrorCode.USER_UNAUTHENTICATED);
        }

        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        var refreshToken = jwtService.generateToken(user, TIME_REFRESH_TOKEN);
        Set<String> roles= new HashSet<>() ;
        for(Role x : user.getRoles()){
                roles.add(x.getName()) ;
        }
        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userName(authenticationRequest.getUsername())
                .roleName(roles)
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .expRefreshToken(new Timestamp(System.currentTimeMillis() + TIME_REFRESH_TOKEN))
                .build();
    }


    @Transactional
    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if(jwtService.isTokenExpired(refreshToken)){
            throw new AppException(ErrorCode.USER_TOKEN_INCORRECT);
        }
        String username= jwtService.extractUsername(refreshToken);
        User user= userRepository.findUserByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        refreshToken = jwtService.generateToken(user, TIME_REFRESH_TOKEN);
        Set<String> roles= new HashSet<>() ;
        for(Role x : user.getRoles()){
            roles.add(x.getName()) ;
        }
        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userName(username)
                .roleName(roles)
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .expRefreshToken(new Timestamp(System.currentTimeMillis() + TIME_REFRESH_TOKEN))
                .build();
    }

    @Override
    @Async
    public void sendMail(String username) {
        Integer code = getCode();

        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        final String to = user.getEmail();

        MimeMessage mimeMessage= javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        try {
            message.setFrom("presidentquyen@gmail.com", "HIT LEARNING");
            message.setTo(to);
            message.setSubject("Mã khôi phục mật khẩu- HIT LEARNING");
            message.setText("<p style = 'font-size: 24px' >HL- " + code + " là  mã xác thực tài khoản của bạn, vui lòng không cung cấp cho người lạ " + "</p>", true);
            message.setSentDate(new Date(System.currentTimeMillis()));
            javaMailSender.send(message.getMimeMessage());
            redisRepository.set(username, code);
            redisRepository.setTTL(username, 1000*60L); // 60 giay
        }catch (Exception e){
            throw new AppException(ErrorCode.EMAIL_NOT_FOUND);
        }
    }
    private Integer getCode(){
        Random random = new Random();
        int ans = random.nextInt(899999);
        return 100000+ans;
    }

    @Override
    public boolean verifyCode(String username, int code) {
        System.out.println(username + " " + code);
        Object codeDb= redisRepository.get(username);
        if (codeDb != null) {
            Integer codeByUsername = (Integer) codeDb;
            if (codeByUsername.equals(code)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void logout(String token) {
        redisRepository.set(token, token); // redis bth
        redisRepository.setTTL(token, jwtService.getExpirationToken(token));
    }
}
