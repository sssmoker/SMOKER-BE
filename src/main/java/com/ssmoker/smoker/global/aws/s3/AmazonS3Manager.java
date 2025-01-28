package com.ssmoker.smoker.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssmoker.smoker.global.configuration.AmazonConfig;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(
                    new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), objectMetadata));
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }
    // 리뷰 사진 저장은 해당 url 을 사용하여 저장
    public String generateReviewKeyName(String uuid) {
        return amazonConfig.getReviewPath() + '/'  + uuid;
    }
    // 프로필 사진 저장은 해당 url 을 사용하여 저장
    public String generateProfileKeyName(String uuid) {
        return amazonConfig.getProfilePath() + '/'  + uuid;
    }
    // 흡연 구역 사진 저장은 해당 url 을 사용하여 저장
    public String generateSmokingAreaKeyName(String keyName, String uuid) {
        return amazonConfig.getSmokingAreaPath() + '/' + keyName + uuid;
    }
}
