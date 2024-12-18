package com.example.hit_learning.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

public class ItemRequest {
    @NotBlank
    String name ;
    @NotBlank
    String description ;
    @NotBlank
    String sectionId ;
    MultipartFile file ;
}
