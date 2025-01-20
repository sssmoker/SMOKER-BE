package com.ssmoker.smoker.test;

import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.aws.s3.AmazonS3Manager;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.jsonwebtoken.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final AmazonS3Manager amazonS3Manager;
    private final TestRepository testRepository;

    @GetMapping("/test/sqlException/{testId}")
    public ApiResponse<Integer> testSqlException(@PathVariable Integer testId) {
        return ApiResponse.onSuccess(testRepository.testQuery(testId));
    }

    @GetMapping("/test/{status}")
    public ApiResponse<String> test(@PathVariable String status) {
        if (status.equals("error")) {
            throw new SmokerBadRequestException(ErrorStatus.TEST);
        }
        return ApiResponse.onSuccess("success");
    }

    @GetMapping("/test2")
    public ApiResponse<String> test2(@AuthUser Long memberId) {
        System.out.println(memberId);
        return ApiResponse.onSuccess("success");
    }

    @PostMapping(value = "/test/imageUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> imageUpload(@ModelAttribute TestRequestDto requestDto) {
        final String imageUrl = upload(requestDto);
        return ApiResponse.onSuccess(imageUrl);
    }

    // 원래는 서비스 클래스에 있어야할 코드
    private String upload(TestRequestDto requestDto) {
        try {
            // 중복된 이름의 파일이 있는 경우 처리를 위해 uuid 추가
            final String uuid = UUID.randomUUID().toString();
            final String keyName = amazonS3Manager.generateReviewKeyName(uuid);
            final String imageUrl = amazonS3Manager.uploadFile(keyName, requestDto.getMultipartFile());
            Test test = requestDto.of(imageUrl);
            testRepository.save(test);
            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 오류");
        }
    }

}
