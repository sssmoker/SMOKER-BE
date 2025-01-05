package com.ssmoker.smoker;

import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.exception.handler.TestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/{status}")
    public ApiResponse<String> test(@PathVariable String status) {
        if (status.equals("error")) {
            throw new TestHandler(ErrorStatus.TEST);
        }
        return ApiResponse.onSuccess("success");
    }
}
