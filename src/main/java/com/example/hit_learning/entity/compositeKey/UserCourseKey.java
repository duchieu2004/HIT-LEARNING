package com.example.hit_learning.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@Embeddable
public class UserCourseKey implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "course_id")
    private String courseId;
}
