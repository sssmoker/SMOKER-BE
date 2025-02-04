package com.ssmoker.smoker.domain.question.converter;

import com.ssmoker.smoker.domain.question.domain.Question;
import com.ssmoker.smoker.domain.question.dto.QuestionResponse;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {

    public static QuestionResponse.QuestionViewResponse toQuestionViewDto(Question question) {
        return QuestionResponse.QuestionViewResponse.builder()
                .title(question.getTitle())
                .updatedAt(question.getCreatedAt())
                .questionId(question.getId())
                .build();
    }

    public static QuestionResponse.QuestionListResponse toQuestionListDto(Page<Question> questions) {
        List<QuestionResponse.QuestionViewResponse> questionList = questions.stream()
                .map(QuestionConverter::toQuestionViewDto).collect(Collectors.toList());
        return QuestionResponse.QuestionListResponse.builder()
                .questionList(questionList)
                .size(questions.getTotalPages())
                .isFirst(questions.isFirst())
                .isLast(questions.isLast())
                .totalElements(questions.getTotalElements())
                .totalPage(questions.getTotalPages())
                .build();
    }

    public static QuestionResponse.QuestionDetailResponse toQuestionDetailDto(Question question) {
        return QuestionResponse.QuestionDetailResponse.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .updatedAt(question.getCreatedAt())
                .content(question.getContent())
                .build();
    }
}
