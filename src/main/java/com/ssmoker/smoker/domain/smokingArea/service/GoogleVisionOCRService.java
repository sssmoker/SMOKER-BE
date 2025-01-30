package com.ssmoker.smoker.domain.smokingArea.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.ssmoker.smoker.global.aws.s3.AmazonS3Manager;
import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleVisionOCRService {

    private final AmazonS3Manager amazonS3Manager;

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
        if (text == null || text.isEmpty()) {
            return false;
        }

        String lowerText = text.toLowerCase();

        Pattern noSmokingPattern = Pattern.compile("\\bno\\s+smoking\\b|\\bno\\s+smoking\\s+zone\\b", Pattern.CASE_INSENSITIVE);
        Matcher noSmokingMatcher = noSmokingPattern.matcher(lowerText);
        if (noSmokingMatcher.find()) return false;

        Pattern smokingPattern = Pattern.compile("\\b흡연\\b|\\bsmoking\\s+area\\b|\\bsmoking\\s+zone\\b", Pattern.CASE_INSENSITIVE);
        Matcher smokingMatcher = smokingPattern.matcher(lowerText);
        return smokingMatcher.find();
    }


    // S3에 이미지 업로드
    public String uploadSmokingAreaImage(MultipartFile file) {
        final String uuid = UUID.randomUUID().toString();
        final String keyName = amazonS3Manager.generateSmokingAreaKeyName("예시 이름", uuid);

        return amazonS3Manager.uploadFile(keyName, file);
    }
}
