package com.ssmoker.smoker.domain.inactivateUsers.domain;

import com.ssmoker.smoker.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InactivateUsers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(nullable = false, unique = true)
    private Long inactiveUserId; //탈퇴 유저 ID

}
