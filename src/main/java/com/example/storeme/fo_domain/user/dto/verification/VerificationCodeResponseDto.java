package com.example.storeme.fo_domain.user.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 코드 요청에 대한 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class VerificationCodeResponseDto {

    private int timeLimit;
}