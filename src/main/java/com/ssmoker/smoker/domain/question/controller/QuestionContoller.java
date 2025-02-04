package com.ssmoker.smoker.domain.question.controller;

import com.ssmoker.smoker.domain.question.dto.QuestionResponse;
import com.ssmoker.smoker.domain.question.service.QuestionService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/question")
public class QuestionContoller {

    private final QuestionService questionService;

    @Operation(summary = "자주 묻는 질문 리스트 조회", description = "페이지는 1부터 시작합니다.")
    @GetMapping
    public ApiResponse<QuestionResponse.QuestionListResponse> getQnAList(@RequestParam Integer Page){
        QuestionResponse.QuestionListResponse result = questionService.getQuestionList(Page);
        return ApiResponse.of(SuccessStatus.QUESTION_OK,result);
    }

    @Operation(summary = "자주 묻는 질문 세부사항 조회")
    @GetMapping("/detail/{questionId}")
    public ApiResponse<QuestionResponse.QuestionDetailResponse> getQnADetail(@RequestParam Long questionId){
        QuestionResponse.QuestionDetailResponse result = questionService.getQuestionDetail(questionId);
        return ApiResponse.of(SuccessStatus.QUESTION_DETAIL_OK,result);
    }
}
