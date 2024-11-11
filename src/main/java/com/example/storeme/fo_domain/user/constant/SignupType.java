package com.example.storeme.fo_domain.user.constant;

import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum SignupType {
    APP,
    KAKAO;

    @JsonCreator
    public static SignupType of(String signupType) {
        for (SignupType type : values()) {
            if (type.name().equals(signupType)) {
                return type;
            }
        }
        log.error("Invalid signupType: {}", signupType);
        throw new UserException(ErrorStatus._BAD_REQUEST);
    }
}
