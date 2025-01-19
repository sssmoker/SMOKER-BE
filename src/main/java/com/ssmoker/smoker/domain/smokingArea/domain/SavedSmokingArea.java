package com.ssmoker.smoker.domain.smokingArea.domain;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SavedSmokingArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "smoking_area_id", nullable = false)
    private SmokingArea smokingArea;
}
