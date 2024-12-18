package com.example.hit_learning.dto.response;

import com.example.hit_learning.dto.request.ItemRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse extends ItemRequest {
    String id ;
    String sectionName ;
    String videoName ;
    String videoId ;
    int view ;
}
