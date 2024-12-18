package com.example.hit_learning.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class OrderDTO {
    String properties ;
    @Builder.Default
    Sort.Direction direction= Sort.Direction.ASC ;
}
