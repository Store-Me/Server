package com.example.storeme.global.config.s3.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.exception.GeneralException;

/**
 * 이미지 파일 처리 관련 예외 클래스
 */
public class ImageFileException extends GeneralException {
    public ImageFileException(BaseErrorCode code) {
        super(code);
    }
}
