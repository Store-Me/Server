package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * App 계정 Id 중복확인 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class AccountIdCheckRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.ACCOUNT_ID_REGEX, message = ValidationConstant.ACCOUNT_ID_MSG)
    private String accountId;
}
