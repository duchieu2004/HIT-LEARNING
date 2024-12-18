package com.example.hit_learning.dto.response;

import com.example.hit_learning.dto.request.CourseRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CourseResponse extends CourseRequest {
    String videoName ;
    String videoId ;
    String id ;
    String leaderName ;
    String leaderId;
    LocalDateTime createdAt ;
}
