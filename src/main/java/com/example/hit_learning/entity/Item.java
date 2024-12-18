package com.example.hit_learning.entity;

import com.example.hit_learning.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Indexed
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;

    @Column(nullable = false)
    @FullTextField(analyzer = "vietnameseAnalyzer")
    private String name ;

    @Column(columnDefinition = "text")
    @FullTextField(analyzer = "vietnameseAnalyzer")
    private String description ;

    private String videoId ;
    private String videoName ;

    @Builder.Default
    private Integer view= 0;

    @ManyToOne @JoinColumn(name = "section_id")
    private Section section;

    @Version
    int version ;

    @OneToMany(mappedBy = "item",fetch=FetchType.LAZY)
    @JsonIgnore
    private List<LikeVideo> likes=new ArrayList<>();

    @OneToMany(mappedBy = "item",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments=new ArrayList<>();

    public void addComment(Comment comment) {
        if(comments==null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setItem(this);
    }

}
