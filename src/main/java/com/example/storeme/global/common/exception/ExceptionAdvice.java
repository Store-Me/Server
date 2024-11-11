package com.example.storeme.global.common.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * GeneralException을 처리하는 메서드
     *
     * @param generalException 커스텀 예외의 최고 조상 클래스
     * @param webRequest       client 요청 객체
     * @return client 응답 객체
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleGeneralException(GeneralException generalException, WebRequest webRequest) {
        BaseErrorCode errorCode = generalException.getErrorCode();
        return handleGeneralExceptionInternal(generalException, errorCode, HttpHeaders.EMPTY, webRequest);
    }

    /**
     * ConstraintViolationException을 처리하는 메서드
     *
     * @param constraintViolationException 검증 예외
     * @param request                      client 요청 객체
     * @return client 응답 객체
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException constraintViolationException, WebRequest request) {

        List<String> errorMessages = constraintViolationException.getConstraintViolations().stream()
                .map(violation -> Optional.ofNullable(violation.getMessage()).orElse(""))
                .toList();

        return handleConstraintExceptionInternal(constraintViolationException, ErrorStatus._VALIDATION_ERROR, HttpHeaders.EMPTY, request,
                errorMessages);
    }

    /**
     * MethodArgumentNotValidException을 처리하는 메서드
     * <p>
     * ResponseEntityExceptionHandler의 메서드를 오버라이딩하여 사용한다.
     *
     * @param methodArgumentNotValidException 컨트롤러 메서드의 파라미터 객체에 대한 검증 예외
     * @param headers                         헤더 객체
     * @param status                          HttpStatusCode 값
     * @param request                         client 요청 객체
     * @return client 응답 객체
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage)
                            -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleArgsExceptionInternal(methodArgumentNotValidException, HttpHeaders.EMPTY, ErrorStatus._VALIDATION_ERROR, request, errors);
    }

    /**
     * 나머지 모든 예외들을 처리하는 메서드
     *
     * @param e       Exception을 상속한 예외 객체
     * @param request client 요청 객체
     * @return client 응답 객체
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleGlobalException(Exception e, WebRequest request) {

        return handleGlobalExceptionInternal(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(), request);
    }

    // GeneralException에 대한 client 응답 객체를 생성하는 메서드
    private ResponseEntity<Object> handleGeneralExceptionInternal(Exception e, BaseErrorCode errorCode,
                                                                  HttpHeaders headers, WebRequest webRequest) {

        log.error("GeneralException captured in ExceptionAdvice", e);

        ResponseDto<Object> body = ResponseDto.onFailure(errorCode);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCode.getHttpStatus(),
                webRequest
        );
    }

    // ConstraintViolationException에 대한 client 응답 객체를 생성하는 메서드
    private ResponseEntity<Object> handleConstraintExceptionInternal(Exception e, ErrorStatus errorStatus,
                                                                     HttpHeaders headers, WebRequest request,
                                                                     List<String> errorMessages) {

        log.error("ConstraintViolationException captured in ExceptionAdvice", e);

        ResponseDto<Object> body = ResponseDto.onFailure(errorStatus.getCode(), errorStatus.getMessage(), errorMessages);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorStatus.getHttpStatus(),
                request
        );
    }

    // MethodArgumentNotValidException에 대한 client 응답 객체를 생성하는 메서드
    private ResponseEntity<Object> handleArgsExceptionInternal(Exception e, HttpHeaders headers, ErrorStatus errorStatus,
                                                               WebRequest request, Map<String, String> errorArgs) {
        log.error("MethodArgumentNotValidException captured in ExceptionAdvice", e);

        ResponseDto<Object> body = ResponseDto.onFailure(errorStatus.getCode(), errorStatus.getMessage(), errorArgs);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorStatus.getHttpStatus(),
                request
        );
    }

    // 나머지 모든 예외에 대한 client 응답 객체를 생성하는 메서드
    private ResponseEntity<Object> handleGlobalExceptionInternal(Exception e, ErrorStatus errorStatus,
                                                                 HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception captured in ExceptionAdvice", e);

        ResponseDto<Object> body = ResponseDto.onFailure(errorStatus);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }
}


