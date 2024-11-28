package com.example.storeme.fo_domain.storeimage.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.exception.GeneralException;

/**
 * 가게 사진과 관련된 예외를 처리하는 클래스
 */
public class StoreImageException extends GeneralException {
    public StoreImageException(BaseErrorCode code) {
        super(code);
    }
}
