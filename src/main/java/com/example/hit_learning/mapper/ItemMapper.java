package com.example.hit_learning.mapper;

import com.example.hit_learning.dto.request.ItemRequest;
import com.example.hit_learning.dto.response.ItemResponse;
import com.example.hit_learning.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemRequest request) ;
    void updItem(@MappingTarget Item item , ItemRequest request) ;
    @Mapping(target = "sectionName" , source = "section.name")
    ItemResponse toResponse(Item item) ;
}
