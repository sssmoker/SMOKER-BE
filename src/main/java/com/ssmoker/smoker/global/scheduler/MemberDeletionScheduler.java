package com.ssmoker.smoker.global.scheduler;

import com.ssmoker.smoker.domain.DeactivateUsers.repository.DeactivatedUsersRepository;
import com.ssmoker.smoker.domain.member.domain.MemberStatus;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MemberDeletionScheduler {

    private final DeactivatedUsersRepository deactivatedUsersRepository;

    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional
    public void clearBlackList() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        deactivatedUsersRepository.deleteByCreatedAtBefore(twoHoursAgo);
        System.out.println("BlackList cleared");
    }
}
