package com.ssmoker.smoker.domain.member.repository;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    void deleteByStatusAndDeactivationDateBefore(MemberStatus status, LocalDateTime deactivationDate);
}
