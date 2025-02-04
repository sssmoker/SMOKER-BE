package com.ssmoker.smoker.domain.member.domain;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.smokingArea.domain.SavedSmokingArea;
import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(nullable = false)
    private String nickName; //닉네임

    @Column(nullable = false)
    private String email; //이메일

    @Column(nullable = false)
    private Integer updateCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private MemberStatus status; // 유저 상태

    private String profileImageUrl; //프로필 이미지

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>(); // 작성 리뷰

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSmokingArea> savedSmokingAreas = new ArrayList<>();

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", updateCount=" + updateCount +
                ", status=" + status +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", reviews=" + reviews +
                '}';
    }
}
