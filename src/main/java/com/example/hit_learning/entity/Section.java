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
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Indexed
public class Section extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @FullTextField(analyzer = "vietnameseAnalyzer")
    private String name;

    @Column(columnDefinition = "text")
    @FullTextField(analyzer = "vietnameseAnalyzer")
    private String description ;

    private int location = 0 ;

    @ManyToOne(fetch= FetchType.EAGER) @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "section",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Item> items;
}
