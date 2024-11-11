package com.example.storeme.fo_domain.user.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * App 계정 Id 중복확인 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AccountIdCheckResponseDto {

    private Boolean isDuplicated;
}
