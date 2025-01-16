package com.ssmoker.smoker.global.scheduler;

import com.ssmoker.smoker.domain.blackList.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BlackListDeletionScheduler {

    private final BlackListRepository blackListRepository;

    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional
    public void clearBlackList() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusDays(1);
        blackListRepository.deleteByCreatedAtBefore(twoHoursAgo);
        System.out.println("BlackList cleared");
    }
}
