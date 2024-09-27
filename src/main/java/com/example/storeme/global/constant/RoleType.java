package com.example.storeme.global.constant;

import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.exception.GeneralException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {

    ROLE_USER("일반 유저"), // 일반 유저
    ROLE_STORE_OWNER("자영업자 유저"), // 자영업자 유저
    ROLE_ADMIN("관리자 유저"); // 관리자 유저

    private final String roleName;

    @JsonCreator
    public static RoleType from(String name) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getRoleName().equals(name)) {
                return roleType;
            }
        }
        throw new GeneralException(ErrorStatus._ROLE_TYPE_NOT_FOUND);
    }

    @JsonValue
    public String getRoleName(){
        return roleName;
    }
}
