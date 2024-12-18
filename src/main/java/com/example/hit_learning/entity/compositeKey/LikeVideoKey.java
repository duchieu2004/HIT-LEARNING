package com.example.hit_learning.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class LikeVideoKey implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "item_id")
    private String itemId;
}
