package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Kakao 계정 회원가입 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class KakaoSignupRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.KAKAO_ID_REGEX, message = ValidationConstant.KAKAO_ID_MSG)
    private String kakaoId;

    @NotNull
    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String phoneNumber;

    @NotNull
    @Size(max = 20, message = ValidationConstant.NICKNAME_MSG)
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
