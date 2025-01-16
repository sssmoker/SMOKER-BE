package com.ssmoker.smoker.domain.inactivateUsers.repository;

import com.ssmoker.smoker.domain.inactivateUsers.domain.DeactivatedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeactivatedUsersRepository extends JpaRepository<DeactivatedUsers,Long> {
}
