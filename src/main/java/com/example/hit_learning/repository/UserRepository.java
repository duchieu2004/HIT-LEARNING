package com.example.hit_learning.repository;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    User findByUsername(String username);

    Optional<User> findUserByUsername(String username);

    User findByEmail(String email);

    Page<User> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
