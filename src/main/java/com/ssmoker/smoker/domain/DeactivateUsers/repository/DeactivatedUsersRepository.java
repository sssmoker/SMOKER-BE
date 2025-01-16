package com.ssmoker.smoker.domain.DeactivateUsers.repository;

import com.ssmoker.smoker.domain.DeactivateUsers.domain.DeactivatedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DeactivatedUsersRepository extends JpaRepository<DeactivatedUsers,Long> {
    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
