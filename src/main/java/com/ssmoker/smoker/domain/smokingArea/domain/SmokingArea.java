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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SmokingArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String smokingAreaName;

    @Embedded
    private Location location;

    @Embedded
    private Feature feature;

    private String imageUrl;

    // 공공기관 데이터와 사용자 데이터를 분리 (수정 가능)
    private Boolean isOpenData = false;

    private String areaType;

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpdatedHistory> updatedHistories = new ArrayList<>();

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSmokingArea> savedSmokingAreas = new ArrayList<>();

    public void updateFeature(Feature updatedFeature) {
        this.feature = this.feature.update(updatedFeature);
    }

    public SmokingArea(String addressName, Location location, Feature feature, String areaType) {
        this.smokingAreaName = addressName;
        this.location = location;
        this.feature = feature;
        this.areaType = areaType;
        this.isOpenData = true;
    }
}
