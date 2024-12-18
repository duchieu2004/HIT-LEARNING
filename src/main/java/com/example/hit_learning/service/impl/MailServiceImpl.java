package com.example.hit_learning.service.impl;

import com.example.hit_learning.entity.User;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService {


    @Override
    public void sendMail(User user) {

    }
}
