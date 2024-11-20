package com.example.storeme.fo_domain.user.dto.verification;

import com.example.storeme.fo_domain.user.constant.SignupType;
import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 인증 코드 확인 요청 Dto 클래스
 */
@NoArgsConstructor
@Getter
public class ConfirmCodeRequestDto {

    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String phoneNumber;

    @Pattern(regexp = ValidationConstant.VERIFICATION_CODE_REGEX,
            message = ValidationConstant.VERIFICATION_CODE_MSG)
    private String verificationCode;


}