package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * App 계정 연동 회원가입 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class AppLinkSignupRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.ACCOUNT_ID_REGEX, message = ValidationConstant.ACCOUNT_ID_MSG)
    private String accountId;

    @NotNull
    @Pattern(regexp = ValidationConstant.PASSWORD_REGEX, message = ValidationConstant.PASSWORD_MSG)
    private String password;

    @NotNull
    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String phoneNumber;

    @Pattern(regexp = ValidationConstant.VERIFICATION_CODE_REGEX,
            message = ValidationConstant.VERIFICATION_CODE_MSG)
    private String verificationCode;
}
