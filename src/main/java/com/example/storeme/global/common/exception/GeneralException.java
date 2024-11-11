package com.example.storeme.global.common.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀 예외의 최고 조상 클래스
 */
@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode errorCode;

}
