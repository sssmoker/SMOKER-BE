package com.ssmoker.smoker.domain.updatedHistory.dto;

import java.time.LocalDateTime;

public record UpdatedHistoryResponse(String memberName,
                                     int updateCount,
                                     LocalDateTime updatedAt) {

}
