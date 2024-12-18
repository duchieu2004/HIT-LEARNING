package com.example.hit_learning.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionRequest {
    @NotBlank
    String name ;
    @NotBlank
    String description ;
    @NotBlank
    String courseId ;
    @NotNull
    Integer location ;
}
