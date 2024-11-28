package com.example.storeme.fo_domain.store.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.exception.GeneralException;

/**
 * 가게와 관련된 예외를 처리하는 클래스
 */
public class StoreException extends GeneralException {
    public StoreException(BaseErrorCode code) {
        super(code);
    }
}
