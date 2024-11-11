package com.example.storeme.fo_domain.user.dto.verification;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 인증 코드 확인 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ConfirmCodeResponseDto {

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
