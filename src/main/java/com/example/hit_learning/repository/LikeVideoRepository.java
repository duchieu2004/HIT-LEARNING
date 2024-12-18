package com.example.hit_learning.repository;

import com.example.hit_learning.dto.response.LikeResponse;
import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.LikeVideo;
import com.example.hit_learning.entity.compositeKey.LikeVideoKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeVideoRepository extends JpaRepository<LikeVideo, LikeVideoKey> {
    List<LikeVideo> findAllByItem(Item item);
}
