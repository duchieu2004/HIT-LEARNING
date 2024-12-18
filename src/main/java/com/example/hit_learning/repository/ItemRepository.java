package com.example.hit_learning.repository;

import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.Section;
import jakarta.persistence.LockModeType;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    Optional<Item> findById(String id);
    Page<Item> findAllBySection(Section section, Pageable pageable) ;


    @Transactional
    @Modifying
    @Query(value = "UPDATE Item it " +
            "SET it.view= :view + 1 " +
            ", it.version = :version + 1 " +
            "WHERE it.id = :itemId " +
            "AND  it.version = :version")
    void updateView(int view, String itemId, int version) ;
}
