package com.example.storeme.fo_domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 인증 속성을 관리하는 enum 클래스
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public enum VerificationProperty {
    TIME_LIMIT(300), // 인증코드 유효시간(단위: sec)
    CODE_LENGTH(6), // 인증코드의 길이
    MAX_CODE_CONFIRM_ATTEMPTS(3), // 인증코드의 인증 제한 횟수
    MAX_CODE_ISSUE_ATTEMPTS(5), // 인증코드 발급 제한 횟수
    CONFIRMED_CODE_TIME_LIMIT(60*60); // 인증된 인증코드 유효시간(1시간)

    private final int value;

}