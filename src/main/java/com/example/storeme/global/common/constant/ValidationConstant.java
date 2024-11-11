package com.example.storeme.global.common.constant;

/**
 * 검증 로직에서 사용되는 상수를 관리하는 클래스
 */
public class ValidationConstant {
    public static final String PHONE_NUMBER_REGEX = "^01[016789]\\d{8}$";
    public static final String PHONE_NUMBER_MSG = "잘못된 전화번호 형식입니다.";

    public static final String VERIFICATION_CODE_REGEX = "^[a-zA-Z0-9]{6}$";
    public static final String VERIFICATION_CODE_MSG = "잘못된 인증코드 형식입니다.";

    // 최소 4자에서 최대 20자까지 허용
    // 영어 대문자, 소문자, 숫자 허용
    public static final String ACCOUNT_ID_REGEX = "^[a-zA-Z0-9]{4,20}$";
    public static final String ACCOUNT_ID_MSG = "잘못된 아이디 형식입니다.";

    // 최소 1자리에서 최대 19자리까지 숫자만 허용
    public static final String KAKAO_ID_REGEX = "^[0-9]{1,19}$";
    public static final String KAKAO_ID_MSG = "유효하지 않은 카카오 ID입니다.";

    // 최소 8자에서 최대 20자까지 허용
    // 적어도 하나의 대문자, 소문자, 숫자, 특수문자 포함
    // 허용할 특수문자: @, #, $, %, &, *, !, ^
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*!])[A-Za-z\\d@#$%^&*!]{8,20}$";
    public static final String PASSWORD_MSG = "잘못된 비밀번호 형식입니다.";

    public static final String NICKNAME_MSG = "잘못된 닉네임 형식입니다.";

    public static final String URL_REGEX =
            "^(https:\\/\\/[a-zA-Z0-9.-]+\\.s3\\.[a-zA-Z0-9.-]+\\.amazonaws\\.com\\/[a-zA-Z0-9\\/\\-_\\.]+)$";
    public static final String URL_MSG = "유효하지 않은 S3 URL입니다.";

    public static final String MIN_VALUE_MSG = "값은 최소 {value}이어야 합니다.";
    public static final String MAX_VALUE_MSG = "값은 최대 {value}이어야 합니다.";
}
