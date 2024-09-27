package com.example.storeme.global.common.code;

import com.example.storeme.global.common.response.ResponseDto;

public interface BaseErrorCode {
    public ResponseDto.ErrorReasonDto getReason();

    public ResponseDto.ErrorReasonDto getReasonHttpStatus();

    public String getCode();
    public String getErrorMsg();
}
