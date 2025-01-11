package com.example.storeme.global.common.constant;

public enum AreaCode {
    SEOUL("02"),
    INCHEON("032"),
    DAEJEON("042"),
    BUSAN("051"),
    ULSAN("052"),
    GYEONGSANGNAM("053"),
    GYEONGNAM("062"),
    JEJU("064"),
    GYEONGGI("031"),
    GANGWON("033"),
    CHUNGCHEONGNAM("041"),
    CHUNGCHEONGBU("043"),
    GYEONGBU("054"),
    GYEONGNAMBU("055"),
    JEOLLANAM("061"),
    JEOLLABU("063");

    private final String code;

    AreaCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean contains(String areaCode) {
        for (AreaCode code : AreaCode.values()) {
            if (code.getCode().equals(areaCode)) {
                return true;
            }
        }
        return false;
    }
}