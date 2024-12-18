package com.example.hit_learning.repository;

import com.example.hit_learning.entity.CourseUser;
import com.example.hit_learning.entity.compositeKey.UserCourseKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseUserRepository extends JpaRepository<CourseUser, UserCourseKey> {
    @Query("FROM CourseUser cs " +
            "WHERE cs.course.id = :courseId ")
    Page<CourseUser> findAllByCourse(@Param("courseId") String courseId, Pageable pageable);
}
