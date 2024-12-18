package com.example.hit_learning.entity;

import com.example.hit_learning.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Indexed
public class Course extends BaseEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @FullTextField(analyzer = "vietnameseAnalyzer")
    private String name;

    @Column(columnDefinition = "text")
    @FullTextField(analyzer = "vietnameseAnalyzer")
    private String description ;

    private String videoName;
    private String videoId;
    private Boolean isPrivate;

    @Builder.Default
    private Boolean available= true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user ;

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseUser> users=new ArrayList<>();

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY , cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Section> sections=new ArrayList<>();

}
