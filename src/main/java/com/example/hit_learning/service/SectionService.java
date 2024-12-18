package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.request.SectionRequest;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.dto.response.SectionResponse;
import com.example.hit_learning.entity.Section;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectionService {
    SectionResponse addSection(SectionRequest request) ;
    SectionResponse updSection(String sectionId, SectionRequest request) ;
    List<SectionResponse> delSections(String... sectionIds) ;
    Section findById(String sectionId) ;
    PageResponse<SectionResponse> findAll(PageRequest request) ;
    PageResponse<SectionResponse> findAllByCourse(String courseId, PageRequest request) ;
}
