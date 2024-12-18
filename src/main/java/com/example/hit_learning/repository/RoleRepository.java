package com.example.hit_learning.repository;

import com.example.hit_learning.entity.ERole;
import com.example.hit_learning.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);
}
