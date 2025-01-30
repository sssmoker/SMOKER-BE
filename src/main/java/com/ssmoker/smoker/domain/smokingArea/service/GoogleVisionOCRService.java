package com.ssmoker.smoker.domain.smokingArea.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GoogleVisionOCRService {
    // 이미지 내 text 검출
    public String detectText(MultipartFile file) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(file.getInputStream());

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            if (responses.isEmpty()) throw new GeneralException(ErrorStatus.TEXT_NOT_DETECTED);

            return responses.get(0).getTextAnnotations(0).getDescription().trim();
        }
    }

    // 검출된 text 검증
    public boolean isSmokingArea(String text) {
        if (text.contains("흡연") || text.toLowerCase().contains("smoking area")) return true;

        throw new SmokingAreaNotFoundException(ErrorStatus.SMOKING_KEYWORD_NOT_FOUND);
    }
}
