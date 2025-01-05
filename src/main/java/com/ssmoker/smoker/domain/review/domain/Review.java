package com.ssmoker.smoker.domain.review.domain;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smoking_area_id", nullable = false)
    private SmokingArea smokingArea;

    @Column(nullable = false)
    private Double score;

    @Column(length = 1000, nullable = false)
    private String content;

    private String imageUrl;
}
