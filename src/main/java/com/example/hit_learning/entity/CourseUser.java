package com.example.hit_learning.entity;

import com.example.hit_learning.entity.base.BaseEntity;
import com.example.hit_learning.entity.compositeKey.UserCourseKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourseUser extends BaseEntity {
    @EmbeddedId
    private UserCourseKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
}
