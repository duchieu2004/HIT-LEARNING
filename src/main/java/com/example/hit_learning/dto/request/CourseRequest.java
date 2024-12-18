package com.example.hit_learning.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {
    @NotBlank
    String userId ;
    @NotBlank
    String name;
    @NotBlank
    String description;
    MultipartFile file ;
    Boolean isPrivate;
}
