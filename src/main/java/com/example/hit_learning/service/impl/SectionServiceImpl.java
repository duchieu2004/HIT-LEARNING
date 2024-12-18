package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.request.SectionRequest;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.dto.response.SectionResponse;
import com.example.hit_learning.entity.Course;
import com.example.hit_learning.entity.Section;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.mapper.SectionMapper;
import com.example.hit_learning.repository.CourseRepository;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.SectionRepository;
import com.example.hit_learning.service.SectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    final SectionRepository sectionRepository ;
    final SectionMapper sectionMapper ;
    final CourseRepository courseRepository ;
    final RedisRepository redisRepository ;
    final String KEY = "section" ;
    @Override
    public SectionResponse addSection(SectionRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
        );
        if(sectionRepository.existsSectionByCourseAndLocation(course , request.getLocation())){
            updateLocation(course, request.getLocation());
        }
        Section section = sectionMapper.toSection(request);
        section.setCourse(course);
        section= sectionRepository.save(section);
        this.deleteRedis(KEY);
        var response = sectionMapper.toResponse(section);
        return response;
    }

    @Override
    public SectionResponse updSection(String sectionId, SectionRequest request) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_EXISTED)
        ) ;
        Course course = section.getCourse() ;
        if(!request.getCourseId().equals(course.getId())){
            throw new AppException(ErrorCode.COURSE_NOT_EXISTED) ;
        }
        if(section.getLocation() != request.getLocation()){
            updateLocation(course, request.getLocation());
        }
        sectionMapper.updSection(section, request);
        section = sectionRepository.save(section) ;
        this.deleteRedis(KEY);
        var response = sectionMapper.toResponse(section) ;
        return response;
    }

    private void updateLocation(Course course, int location){
        List<Section> sections = sectionRepository.findAllByCourse(course) ;
        sections.sort(new Comparator<Section>() {
            @Override
            public int compare(Section o1, Section o2) {
                return o1.getLocation()- o2.getLocation();
            }
        });
        for(Section x: sections){
            if(x.getLocation() >= location) {
                x.setLocation(x.getLocation() + 1);
                sectionRepository.save(x);
            }
        }
        this.deleteRedis(KEY);
    }
    @Override
    public List<SectionResponse> delSections(String... sectionIds) {
        List<SectionResponse> responses = new ArrayList<>() ;
        for(String x : sectionIds){
            var xyOptional = sectionRepository.findById(x) ;
            if(xyOptional.isPresent()){
                sectionRepository.delete(xyOptional.get());
                var xyz= sectionMapper.toResponse(xyOptional.get()) ;
                responses.add(xyz) ;
            }
        }
        this.deleteRedis(KEY);
        return responses ;
    }

    @Override
    public Section findById(String sectionId) {
        final String field= "id:"+sectionId;
        Object jsonValue= redisRepository.getHash(KEY, field);
        if(jsonValue == null) {
            var xy = sectionRepository.findById(sectionId).orElseThrow(
                    () -> new AppException(ErrorCode.SECTION_NOT_EXISTED)
            );
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(xy));
            return xy;
        }else{
            Section section = (Section) redisRepository.convertToObject(jsonValue+"", Section.class);
            return section;
        }
    }

    @Override

    public PageResponse<SectionResponse> findAll(PageRequest request) {
        String field="find-all: " + request.toString();
        Object jsonValue = redisRepository.getHash(KEY, field) ;
        if(jsonValue == null) {
            Pageable pageable = request.getPageable();
            Page<Section> page = sectionRepository.findAll(pageable);
            List<SectionResponse> sectionResponses = new ArrayList<>();
            for (Section x : page.getContent()) {
                sectionResponses.add(sectionMapper.toResponse(x));
            }
            var response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders(), sectionResponses, page.getTotalPages());
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        }else{
            PageResponse<SectionResponse> response = (PageResponse<SectionResponse>) redisRepository.convertToObject(jsonValue+"", PageResponse.class);
            return  response;
        }
    }

    @Override

    public PageResponse<SectionResponse> findAllByCourse(String courseId, PageRequest request) {
        String field="find-all-by-course: "  + courseId + "page: " + request.toString();
        Object jsonValue = redisRepository.getHash(KEY, field) ;
        if(jsonValue == null) {
            Course course = courseRepository.findById(courseId).orElseThrow(
                    () -> new AppException(ErrorCode.COURSE_NOT_EXISTED)
            );
            Pageable pageable = request.getPageable();
            Page<Section> page = sectionRepository.findAllByCourse(course, pageable);
            List<SectionResponse> sectionResponses = new ArrayList<>();
            for (Section x : page.getContent()) {
                sectionResponses.add(sectionMapper.toResponse(x));
            }
            var response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders(), sectionResponses, page.getTotalPages());
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        }else{
            PageResponse<SectionResponse> response = (PageResponse<SectionResponse>) redisRepository.convertToObject(jsonValue+"", PageResponse.class);
            return  response;
        }
    }

    private void deleteRedis(String key){
        redisRepository.deleteAll(key);
        redisRepository.deleteAll("item");
    }
}
