package com.ssmoker.smoker.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException exception) {
        log.error("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("입력값이 유효하지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("message: ", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("입력값이 유효하지 않습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception exception, WebRequest request) {
        log.error("message: ", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 에러입니다. 관리자에게 문의해주세요.")
    }

    @ExceptionHandler(SmokerException.class)
    public ResponseEntity smokerExceptionHandler(SmokerException exception) {
        log.warn("message: ", exception);

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }
}