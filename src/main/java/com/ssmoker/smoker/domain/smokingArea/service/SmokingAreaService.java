package com.ssmoker.smoker.domain.smokingArea.service;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.smokingArea.service.dto.SmokingAreaInfoResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmokingAreaService {

    private final SmokingAreaRepository smokingAreaRepository;

    public SmokingAreaInfoResponse getSmokingAreaInfo(Long id) {
        Optional<SmokingArea> smokingArea = smokingAreaRepository.findById(id);
        if (smokingArea.isPresent()) {
            return SmokingAreaInfoResponse.of(smokingArea.get());
        }
        throw new RuntimeException("버그에요");
    }
}
