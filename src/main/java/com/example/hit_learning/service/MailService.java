package com.example.hit_learning.service;

import com.example.hit_learning.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendMail(User user);
}
