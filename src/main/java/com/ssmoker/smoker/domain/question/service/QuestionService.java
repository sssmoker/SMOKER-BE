package com.ssmoker.smoker.domain.question.service;

import com.ssmoker.smoker.domain.question.converter.QuestionConverter;
import com.ssmoker.smoker.domain.question.domain.Question;
import com.ssmoker.smoker.domain.question.dto.QuestionResponse;
import com.ssmoker.smoker.domain.question.repository.QuestionRepository;
import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionResponse.QuestionListResponse getQuestionList(Integer page) {
        if (page == null || page < 1) {
            throw new GeneralException(ErrorStatus.QUESTION_BAD_REQUEST);
        }
        Pageable pageable
                = PageRequest.of(page - 1, 7, Sort.by("updatedAt").descending());
        Page<Question> questions
                = questionRepository.findAll(pageable);
        QuestionResponse.QuestionListResponse questionList = QuestionConverter.toQuestionListDto(questions);
        return questionList;
    }

    public QuestionResponse.QuestionDetailResponse getQuestionDetail(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_FOUND));
        return QuestionConverter.toQuestionDetailDto(question);
    }

}
