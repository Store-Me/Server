package com.example.storeme.fo_domain.user.constant;

/**
 * 유저의 권한 정보
 */
public enum RoleType {

    CUSTOMER, // 손님 유저
    OWNER, // 사장님 유저
    DUAL, // 손님 & 사장님 둘다 가능한 유저
    ADMIN; // 관리자 유저

}
