package com.example.storeme.global.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Redis key의 prefix를 관리하는 enum 클래스
 */
@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {
    REFRESH_TOKEN("REFRESH_TOKEN_"),

    VERIFICATION_CODE("VERIFICATION_CODE:"), // 인증코드의 Redis key prefix
    VERIFICATION_ISSUE_COUNT("VERIFICATION_ISSUE_COUNT:"), // 인증코드 발급 횟수의 Redis key prefix
    VERIFICATION_ATTEMPTS("VERIFICATION_ATTEMPTS:"); // 인증코드 시도 횟수의 Redis key prefix

    private final String prefix;
}
