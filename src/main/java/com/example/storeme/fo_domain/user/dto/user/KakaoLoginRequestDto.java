package com.example.storeme.fo_domain.user.dto.user;

import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Kakao 계정 로그인 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class KakaoLoginRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.KAKAO_ID_REGEX, message = ValidationConstant.KAKAO_ID_MSG)
    private String kakaoId;
}
