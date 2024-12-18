package com.example.hit_learning.dto.response;

import com.example.hit_learning.dto.request.SectionRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionResponse extends SectionRequest {
    String id ;
    String courseName ;
    LocalDateTime createdAt ;
}
