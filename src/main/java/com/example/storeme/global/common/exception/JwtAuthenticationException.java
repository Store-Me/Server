package com.example.storeme.global.common.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * JWT 인증 예외 클래스
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final BaseErrorCode errorCode;

    public JwtAuthenticationException(BaseErrorCode errorCode){
        super(errorCode.getErrorMsg());
        this.errorCode=errorCode;
    }

}
