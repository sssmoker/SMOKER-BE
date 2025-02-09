package com.ssmoker.smoker.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON", "성공입니다."),
    //TODO: 다양한 성공 응답 추가...

    // 회원 가능 관련
    USER_LOGIN_OK(HttpStatus.OK, "AUTH2001", "회원 로그인이 완료되었습니다."),
    USER_LOGOUT_OK(HttpStatus.OK, "AUTH2002", "회원 로그아웃이 완료되었습니다."),
    USER_DELETE_OK(HttpStatus.OK, "AUTH2003", "회원 탈퇴가 완료되었습니다."),
    USER_REFRESH_OK(HttpStatus.OK, "AUTH2004", "토큰 재발급이 완료되었습니다."),

    //map 관련
    MAP_INFO_OK(HttpStatus.OK, "MAP2001", "마커를 위한 흡연 구역 모두를 불러왔습니다."),
    MAP_MARKER_OK(HttpStatus.OK, "MAP2002", "마커 모달을 불러왔습니다."),
    MAP_LIST_OK(HttpStatus.OK, "MAP2003", "목록을 불러왔습니다."),
    MAP_SEARCH_OK(HttpStatus.OK, "MAP2004", "검색이 완료되었습니다."),

    //ocr 관련
    OCR_VERIFY_OK(HttpStatus.OK, "OCR2001", "흡연 구역 검증 완료 및 S3 이미지 저장에 성공했습니다."),

    //notice 관련
    NOTICES_OK(HttpStatus.OK, "NOTICE2001", "공지사항 조회가 완료되었습니다."),
    NOTICE_DETAIL_OK(HttpStatus.OK, "NOTICE2002", "공지사항 세부 조회가 완료되었습니다."),

    // Profile 관련
    NICKNAME_OK(HttpStatus.OK, "PROFILE2001", "닉네임 변경이 완료되었습니다."),
    PROFILE_IMAGE_OK(HttpStatus.OK, "PROFILE2002", "프로필 이미지 변경이 완료되었습니다."),
    PROFILE_OK(HttpStatus.OK, "PROFILE2003", "프로필 조회 성공"),
    PROFILE_REVIEWS_OK(HttpStatus.OK, "PROFILE2004", "내가 쓴 리뷰 조회 성공"),
    PROFILE_UPDATE_OK(HttpStatus.OK, "PROFILE2005", "나의 업데이트 히스토리 조회 성공"),
    // Review 관련
    REVIEW_OK(HttpStatus.OK, "REVIEW2001", "리뷰가 등록 되었습니다."),

    //question 관련
    QUESTION_OK(HttpStatus.OK, "QUESTION2001", "자주 묻는 질문 리스트 조회가 완료되었습니다."),
    QUESTION_DETAIL_OK(HttpStatus.OK, "QUESTION2002", "자주 묻는 질문 세부 조회가 완료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
