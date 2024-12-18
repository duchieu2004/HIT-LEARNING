package com.example.hit_learning.mapper;

import com.example.hit_learning.dto.request.SectionRequest;
import com.example.hit_learning.dto.response.SectionResponse;
import com.example.hit_learning.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    Section toSection(SectionRequest request) ;
    void updSection(@MappingTarget Section section, SectionRequest request) ;

    @Mapping(target = "courseName", source = "course.name")
    SectionResponse toResponse(Section section) ;
}
