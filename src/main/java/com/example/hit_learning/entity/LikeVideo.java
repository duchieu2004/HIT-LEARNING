package com.example.hit_learning.entity;

import com.example.hit_learning.entity.base.BaseEntity;
import com.example.hit_learning.entity.compositeKey.LikeVideoKey;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LikeVideo extends BaseEntity {
    @EmbeddedId
    private LikeVideoKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;
}
