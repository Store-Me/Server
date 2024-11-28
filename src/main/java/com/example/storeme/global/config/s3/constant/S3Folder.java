package com.example.storeme.global.config.s3.constant;

import lombok.Getter;

/**
 * S3 폴더를 관리하는 enum 클래스
 */
@Getter
public enum S3Folder {
    CUSTOMER_PROFILE_IMAGE("customer-profile-image/"),
    STORE_PROFILE_IMAGE("store-profile-image/"),
    STORE_IMAGE("store-image/"),
    STORE_BANNER_IMAGE("store-banner-image/"),
    MENU_IMAGE("menu-image/"),
    COUPON_IMAGE("coupon-image/");

    private final String path;

    S3Folder(String path) {
        this.path = path;
    }
}
