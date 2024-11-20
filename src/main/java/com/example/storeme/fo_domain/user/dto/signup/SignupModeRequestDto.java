package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.fo_domain.user.constant.SignupType;
import com.example.storeme.global.common.constant.ValidationConstant;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 모드 조회 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class SignupModeRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String phoneNumber;

    @NotNull
    private SignupType signupType;
}
