package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.fo_domain.user.constant.RoleType;
import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * App 계정 회원가입 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class AppSignupRequestDto {

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

    @NotNull
    @Length(max = 10, message = ValidationConstant.NICKNAME_MSG)
    private String nickname;

    @NotNull
    @Pattern(regexp = ValidationConstant.URL_REGEX, message = ValidationConstant.URL_MSG)
    private String profileImageUrl;

    @NotNull
    private Boolean privacyConsent;

    @NotNull
    private Boolean marketingConsent;

    @Pattern(regexp = ValidationConstant.VERIFICATION_CODE_REGEX,
            message = ValidationConstant.VERIFICATION_CODE_MSG)
    private String verificationCode;

}
