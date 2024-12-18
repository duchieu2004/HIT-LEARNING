package com.example.hit_learning.entity;

import com.example.hit_learning.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "varchar(120) collate 'utf8_bin'" ,unique = true ,nullable = false)
    private String username;

    @Column(columnDefinition = "varchar(120) collate 'utf8_bin'" , nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String name;

    @Column(nullable = false ,unique = true)
    private String email;

    private String linkFb;

    @Column(columnDefinition = "TINYINT")
    private boolean available;

    private String linkAvatar;

    @Column(columnDefinition = "text")
    private String description;

    private String className;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles=new HashSet<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses = new ArrayList<>() ;


    @OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
    @JsonIgnore
    private List<CourseUser> courseUsers =new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
    @JsonIgnore
    private List<LikeVideo> likes=new ArrayList<>();

    @OneToMany(mappedBy ="user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    public void addComment(Comment comment) {
        if(comments==null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setUser(this);
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> role=  new ArrayList<>();
        for(Role x : roles){
            role.add(new SimpleGrantedAuthority(x.getName()));
        }
        return role;
    }
}
