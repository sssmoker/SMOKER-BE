package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.SMOKING_AREA_NOT_FOUND;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaInfoResponse;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
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
        throw new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND);
    }
}
