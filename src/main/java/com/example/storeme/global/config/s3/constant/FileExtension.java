package com.example.storeme.global.config.s3.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum FileExtension {

    JPG("jpg"),

    JPEG("jpeg"),

    PNG("png");

    private final String extension;

    public static boolean contains(String extension) {
        for (FileExtension fileExtension : FileExtension.values()) {
            if (fileExtension.getExtension().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

}
