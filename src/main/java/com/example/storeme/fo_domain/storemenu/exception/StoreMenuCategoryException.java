package com.example.storeme.fo_domain.storemenu.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.exception.GeneralException;

/**
 * 가게 메뉴 카테고리와 관련된 예외 클래스
 */
public class StoreMenuCategoryException extends GeneralException {
    public StoreMenuCategoryException(BaseErrorCode code) {
        super(code);
    }
}
