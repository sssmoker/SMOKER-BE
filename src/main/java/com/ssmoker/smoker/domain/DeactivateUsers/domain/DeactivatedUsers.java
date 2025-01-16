package com.ssmoker.smoker.domain.DeactivateUsers.domain;

import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DeactivatedUsers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(nullable = false)
    private Long deactiveUserId; //탈퇴 유저 ID

}
