package com.example.storeme.global.common.code;

import com.example.storeme.global.common.dto.ResponseDto;

/**
 * 성공 응답 코드를 관리하는 인터페이스
 */
public interface BaseCode {

    ResponseDto.ReasonDto getReason();

    String getCode();

    String getMsg();

}
