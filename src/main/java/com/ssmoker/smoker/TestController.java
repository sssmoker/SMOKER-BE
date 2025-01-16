package com.ssmoker.smoker;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/{status}")
    public ApiResponse<String> test(@PathVariable String status) {
        if (status.equals("error")) {
            throw new SmokerBadRequestException(ErrorStatus.TEST);
        }
        return ApiResponse.onSuccess("success");
    }

    @GetMapping("/test2")
    public ApiResponse<String> test2(@Parameter(hidden = true) @AuthUser Member member) {
        System.out.println("member.getNickName() = " + member.getNickName());
        return ApiResponse.onSuccess("success");
    }
}
