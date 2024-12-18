package com.example.hit_learning.repository;

import com.example.hit_learning.entity.Course;
import com.example.hit_learning.entity.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, String> {
    List<Section> findAllByCourse(Course course) ;
    Page<Section> findAllByCourse(Course course , Pageable pageable) ;
    boolean existsSectionByCourseAndLocation(Course course, int location) ;
}
