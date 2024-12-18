package com.example.hit_learning.repository;

import com.example.hit_learning.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course , String> {
    Optional<Course> findCourseByIdAndAvailable(String id , boolean available) ;
    Page<Course> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("FROM Course  c " +
            "WHERE c.user.id= :userId ")
    List<Course> findAllCourseByUser(String userId);
}
