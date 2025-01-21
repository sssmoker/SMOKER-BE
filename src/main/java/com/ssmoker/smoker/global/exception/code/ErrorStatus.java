package com.ssmoker.smoker.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    //Test
    TEST(HttpStatus.BAD_GATEWAY, "TEST2025", "예외 처리 테스트입니다."),
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    // Member
    // fixme: 예시라서 수정하셔도 됩니다 !
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    // Review
    REVIEW_BAD_REQUEST(HttpStatus.BAD_REQUEST, "REVIEW4001", "페이지 넘버는 0 이상이어야 합니다."),
    // SmokingArea
    SMOKING_AREA_NOT_FOUND(HttpStatus.BAD_REQUEST, "AREA4001", "흡연 구역이 없습니다."),
    FILTER_NOT_FOUND(HttpStatus.BAD_REQUEST, "AREA4002", "올바르지 않은 필터입니다."),

    // Auth 관련
    AUTH_EXTRACT_ERROR_TEST(HttpStatus.UNAUTHORIZED, "AUTH_000TSET", "토큰 추출에 실패했습니다TEST."),

    AUTH_EXTRACT_ERROR(HttpStatus.UNAUTHORIZED, "AUTH_000", "토큰 추출에 실패했습니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "토큰이 유효하지 않습니다."),
    INVALID_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "AUTH_003", "올바른 아이디나 패스워드가 아닙니다."),
    NOT_EQUAL_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "리프레시 토큰이 다릅니다."),
    NOT_CONTAIN_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_005", "해당하는 토큰이 저장되어있지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_006", "비밀번호가 일치하지 않습니다."),
    INVALID_REQUEST_INFO_KAKAO(HttpStatus.UNAUTHORIZED, "AUTH_007", "카카오 정보 불러오기에 실패하였습니다."),
    INVALID_REQUEST_INFO_GOOGLE(HttpStatus.UNAUTHORIZED, "AUTH_007", "구글 정보 불러오기에 실패하였습니다."),
    AUTH_INVALID_CODE(HttpStatus.UNAUTHORIZED, "AUTH_008", "코드가 유효하지 않습니다."),

    // User 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "존재하지 않는 사용자입니다."),
    USER_EXISTS(HttpStatus.BAD_REQUEST, "USER_002", "이미 존재하는 아이디입니다."),
    USER_DELETE_FAILED(HttpStatus.NOT_FOUND, "USER_003", "회원 탈퇴에 실패했습니다."),

    // Profile 관련
    FORBIDDEN_NICKNAME(HttpStatus.BAD_REQUEST, "PROFILE_001", "사용할 수 없는 닉네임입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "PROFILE_002", "이미 존재하는 닉네임입니다"),
    ;

    // todo: 필요한 예외 추가
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
