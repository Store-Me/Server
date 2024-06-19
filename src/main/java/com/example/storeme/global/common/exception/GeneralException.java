package com.example.storeme.global.common.exception;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.response.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    public ResponseDto.ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ResponseDto.ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
