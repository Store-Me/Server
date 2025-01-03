package com.example.storeme.global.config.s3.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum FileContentType {

    JPEG("image/jpeg"),

    PNG("image/png");

    private final String contentType;

    public static boolean contains(String contentType) {
        for (FileContentType fileContentType : FileContentType.values()) {
            if (fileContentType.getContentType().equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

}
