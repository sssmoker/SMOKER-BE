package com.ssmoker.smoker.domain.updatedHistory.service;

import com.ssmoker.smoker.domain.updatedHistory.dto.UpdatedHistoryResponse;
import com.ssmoker.smoker.domain.updatedHistory.dto.UpdatedHistoryResponses;
import com.ssmoker.smoker.domain.updatedHistory.repository.UpdatedHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatedHistoryService {

    private static final int HISTORY_PAGE_SIZE = 5;

    private final UpdatedHistoryRepository updatedHistoryRepository;

    public UpdatedHistoryResponses getUpdatedHistoryBySmokingAreaId(Long smokingAreaId, int pageNumber) {
        Page<UpdatedHistoryResponse> updatedHistoryPage = updatedHistoryRepository.findPagedMemberDetailsBySmokingAreaId(smokingAreaId,
                PageRequest.of(pageNumber, HISTORY_PAGE_SIZE));
        List<UpdatedHistoryResponse> updatedHistoryList = updatedHistoryPage.getContent();

        return UpdatedHistoryResponses.of(updatedHistoryList, updatedHistoryPage.isLast(), updatedHistoryPage.getNumber());
    }

    public UpdatedHistoryResponses getUpdatedHistoryByMemberId(Long memberId, int pageNumber) {
        Page<UpdatedHistoryResponse> updatedHistoryPage = updatedHistoryRepository.findUpdatedHistoriesByMemberId(memberId,
                PageRequest.of(pageNumber, HISTORY_PAGE_SIZE));
        List<UpdatedHistoryResponse> updatedHistoryList = updatedHistoryPage.getContent();

        return UpdatedHistoryResponses.of(updatedHistoryList, updatedHistoryPage.isLast(), updatedHistoryPage.getNumber());
    }
}
