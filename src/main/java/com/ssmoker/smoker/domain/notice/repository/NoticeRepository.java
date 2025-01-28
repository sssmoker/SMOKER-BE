package com.ssmoker.smoker.domain.notice.repository;

import com.ssmoker.smoker.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
