package com.example.storeme.fo_domain.user.dto.signup;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 모드 조회 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignupModeResponseDto {
    private SignupMode signupMode;

    public enum SignupMode{
        NORMAL_SIGNUP,
        LINK_SIGNUP,
        ALREADY_SIGNED_UP;

        @JsonValue
        public String toString() {
            return name();
        }
    }
}
