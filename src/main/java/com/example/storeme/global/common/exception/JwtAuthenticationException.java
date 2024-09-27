package com.example.storeme.global.common.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final BaseErrorCode code;

    public JwtAuthenticationException(BaseErrorCode code){
        super(code.getErrorMsg());
        this.code=code;
    }
}
