package com.ssmoker.smoker.domain.smokingArea.domain;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.updatedHistory.domain.UpdatedHistory;
import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.Formula;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class SmokingArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String smokingAreaName = "미지정";

    @Embedded
    private Location location;

    @Embedded
    private Feature feature;

    private String imageUrl;

    public SmokingArea(Location location) {
        this.location = location;
        this.feature = new Feature(false, false, false, false);  // null 방지
    }

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpdatedHistory> updatedHistories = new ArrayList<>();

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSmokingArea> savedSmokingAreas = new ArrayList<>();
}
