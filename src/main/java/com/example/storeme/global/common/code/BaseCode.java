package com.example.storeme.global.common.code;

import com.example.storeme.global.common.response.ResponseDto;

public interface BaseCode {
    public ResponseDto.ReasonDto getReason();

    public ResponseDto.ReasonDto getReasonHttpStatus();
}
