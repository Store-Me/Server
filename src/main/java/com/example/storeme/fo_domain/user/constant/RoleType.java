package com.example.storeme.fo_domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 유저의 권한 정보
 */
@Getter
@RequiredArgsConstructor
public enum RoleType {

    ROLE_USER("USER"), // 일반 유저
    ROLE_ADMIN("ADMIN"); // 관리자 유저

    private final String roleType;
}
