package com.whatting.global.error;

import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.CustomJwtException;
import com.whatting.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice // 컨트롤러 예외 잡음
@Slf4j // log 객체 자동 생성
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class) // Business Exception
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception) {
        log.error("handleBusinessException: {}", exception.getMessage(), exception);
        ErrorCode errorCode = exception.getErrorCode(); // 설정한 ErrorCode 정보 가져오기

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Valid Exception
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException: {}", exception.getMessage(), exception);
        String errorMessage = exception.getBindingResult() // 에러 메세지 가져오기
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class) // Excel Exception
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException exception) {
        log.error("파일 용량 초과: {}", exception.getMessage());

        String message = "업로드 가능한 파일 용량을 초과했습니다. (최대 1MB)";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(CustomJwtException.class) // JWT Exception
    public ResponseEntity<ErrorResponse> handleCustomJwtException(CustomJwtException exception) {
        log.warn("JWT Exception : {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleRequestParamException(
            MissingServletRequestParameterException exception) {
        log.error("MissingServletRequestParameterException : {}", exception.getMessage());

        String message = "파라미터 값이 유효하지 않습니다";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(TypeMismatchException exception) {
        log.error("TypeMismatchException : {}", exception.getMessage());

        String message = exception.getRequiredType() == HelpStatus.class
                ? "도움 요청 상태 값이 유효하지 않습니다. 허용 값: UNCHECKED, ACKNOWLEDGED, RESOLVED"
                : "요청 값이 유효하지 않습니다";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), message));
    }
}
