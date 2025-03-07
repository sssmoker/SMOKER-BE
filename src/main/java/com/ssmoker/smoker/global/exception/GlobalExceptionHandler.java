package com.ssmoker.smoker.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
=import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // @RequestBody 바인딩, 보통 발생하면 서버 에러일듯..? 외부로 공개하는 api 가 아닌 경우면
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("message: ", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 에러입니다. 관리자에게 문의해주세요.");
    }

    // @PathVariable, @RequestParam, @ModelAttribute 바인딩
    @ExceptionHandler(BindException.class)
    public ResponseEntity bindException(BindException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getBindingResult());
    }
    // @Validated. @PathVariable, @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(final ConstraintViolationException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("입력값이 유효하지 않습니다.");
    }

    // @Validate. @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("입력값이 유효하지 않습니다.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("잘못된 HTTP 메서드입니다.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity noResourceFoundException(final NoResourceFoundException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("리소스를 찾을 수 없습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(final Exception exception) {
        log.error("message: ", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 에러입니다. 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(SmokerException.class)
    public ResponseEntity smokerExceptionHandler(final SmokerException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }
}
