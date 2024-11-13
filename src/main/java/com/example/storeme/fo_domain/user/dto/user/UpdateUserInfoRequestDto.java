package com.example.storeme.fo_domain.user.dto.user;

import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 유저 정보 수정 요청 Dto 클래스
 */
@NoArgsConstructor
@Getter
public class UpdateUserInfoRequestDto {

    @Pattern(regexp = ValidationConstant.PASSWORD_REGEX, message = ValidationConstant.PASSWORD_MSG)
    private String password;

    @Length(max = 10, message = ValidationConstant.NICKNAME_MSG)
    private String nickname;

    private Boolean privacyConsent;

    private Boolean marketingConsent;
}
