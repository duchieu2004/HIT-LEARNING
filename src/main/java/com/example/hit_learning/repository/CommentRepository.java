package com.example.hit_learning.repository;

import com.example.hit_learning.entity.Comment;
import com.example.hit_learning.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByItem(Item item);
}
