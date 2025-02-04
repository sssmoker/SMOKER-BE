package com.ssmoker.smoker.domain.updatedHistory.dto;

import java.util.List;

public record UpdatedHistoryResponses(List<UpdatedHistoryResponse> updatedHistories,
                                      boolean isLastPage,
                                      int pageNumber) {

    public static UpdatedHistoryResponses of(List<UpdatedHistoryResponse> updatedHistories, boolean isLastPage, int pageNumber) {
        return new UpdatedHistoryResponses(updatedHistories, isLastPage, pageNumber);
    }
}
