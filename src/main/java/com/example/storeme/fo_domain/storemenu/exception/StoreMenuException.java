package com.example.storeme.fo_domain.storemenu.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.exception.GeneralException;

public class StoreMenuException extends GeneralException {
    public StoreMenuException(BaseErrorCode code) {
        super(code);
    }
}
