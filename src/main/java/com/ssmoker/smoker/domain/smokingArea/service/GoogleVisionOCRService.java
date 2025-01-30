package com.ssmoker.smoker.domain.smokingArea.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GoogleVisionOCRService {
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

            StringBuilder resultText = new StringBuilder();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    log.error("오류 발생: {}", res.getError().getMessage());
                    return "오류 발생: " + res.getError().getMessage();
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    resultText.append(annotation.getDescription()).append("\n");
                }
            }

            return resultText.length() > 0 ? resultText.toString().trim() : "텍스트를 감지할 수 없습니다.";
        }
    }
}
