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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SmokingArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String smokingAreaName;

    @Embedded
    private Location location;

    @Embedded
    private Feature feature;

    private String imageUrl;
    /*@Column(nullable = false)
    private Boolean isApproved;*/

    //가상 데이터
    @Formula("(select sa.location_latitude from smoking_area sa where sa.id = id)")
    private Double latitude;

    @Formula("(select sa.location_longitude from smoking_area sa where sa.id = id)")
    private Double longitude;

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpdatedHistory> updatedHistories = new ArrayList<>();

    @OneToMany(mappedBy = "smokingArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSmokingArea> savedSmokingAreas = new ArrayList<>();
}
