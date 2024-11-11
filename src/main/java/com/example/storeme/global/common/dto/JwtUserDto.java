package com.example.storeme.global.common.dto;

import com.example.storeme.fo_domain.user.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Jwt 생성 시에 필요한 유저 데이터를 담는 Dto 클래스
 */
@Getter
@Builder
public class JwtUserDto {
    private final String userId;

    private final RoleType roleType;
}
