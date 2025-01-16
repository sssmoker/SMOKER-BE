package com.ssmoker.smoker.domain.blackList.repository;

import com.ssmoker.smoker.domain.blackList.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BlackListRepository extends JpaRepository<BlackList,Long> {
    void deleteByCreatedAtBefore(LocalDateTime dateTime);
    boolean existsByAccessToken(String accessToken);
}
