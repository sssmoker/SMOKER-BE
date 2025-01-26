package com.ssmoker.smoker.domain.updatedHistory.domain;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class UpdatedHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer updateCount;

    @Enumerated(EnumType.STRING)
    private Action action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "smoking_area_id", nullable = false)
    private SmokingArea smokingArea;

    public UpdatedHistory(Integer updateCount, Action action, Member member, SmokingArea smokingArea) {
        this.updateCount = updateCount;
        this.action = action;
        this.member = member;
        this.smokingArea = smokingArea;
    }

}
