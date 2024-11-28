package com.example.storeme.fo_domain.user.controller;

import com.example.storeme.fo_domain.user.dto.user.AppLoginRequestDto;
import com.example.storeme.fo_domain.user.dto.user.JwtResponseDto;
import com.example.storeme.fo_domain.user.dto.user.KakaoLoginRequestDto;
import com.example.storeme.fo_domain.user.service.LoginService;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "로그인 요청 처리")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login/app")
    @Operation(summary = "App 계정 로그인")
    public ResponseDto<JwtResponseDto> handleAppLogin(@Valid @RequestBody AppLoginRequestDto appLoginRequestDto){
        return ResponseDto.onSuccess(loginService.handleAppLogin(appLoginRequestDto));
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "Kakao 계정 로그인")
    public ResponseDto<JwtResponseDto> handleAppLogin(@Valid @RequestBody KakaoLoginRequestDto kakaoLoginRequestDto){
        return ResponseDto.onSuccess(loginService.handleKakaoLogin(kakaoLoginRequestDto));
    }
}
