package com.example.storeme.fo_domain.user.controller;

import com.example.storeme.fo_domain.user.dto.signup.*;
import com.example.storeme.fo_domain.user.service.SignupService;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
@Tag(name = "회원가입")
public class SignupController {

    private final SignupService signupService;

    /**
     * App 계정 회원가입 요청을 처리하는 메서드
     */
    @Operation(summary = "App 계정 회원가입")
    @PostMapping("/app")
    public ResponseDto<Void> handleAppSignup(@Valid @RequestBody AppSignupRequestDto appSignupRequestDto) {
        signupService.handleAppSignup(appSignupRequestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * Kakao 계정 회원가입 요청을 처리하는 메서드
     */
    @PostMapping("/kakao")
    @Operation(summary = "Kakao 계정 회원가입")
    public ResponseDto<Void> handleKakaoSignup(@Valid @RequestBody KakaoSignupRequestDto kakaoSignupRequestDto) {
        signupService.handleKakaoSignup(kakaoSignupRequestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * App 계정 연동 회원가입 요청을 처리하는 메서드
     */
    @PostMapping("/link/app")
    @Operation(summary = "App 계정 연동 회원가입")
    public ResponseDto<Void> handleKakaoSignup(@Valid @RequestBody AppLinkSignupRequestDto appLinkSignupRequestDto) {
        signupService.handleAppLinkSignup(appLinkSignupRequestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * Kakao 계정 연동 회원가입 요청을 처리하는 메서드
     */
    @PostMapping("/link/kakao")
    @Operation(summary = "Kakao 계정 연동 회원가입")
    public ResponseDto<Void> handleKakaoSignup(@Valid @RequestBody KakaoLinkSignupRequestDto kakaoLinkSignupRequestDto) {
        signupService.handleKakaoLinkSignup(kakaoLinkSignupRequestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * User의 AccountId에 대한 중복 검사를 처리하는 메서드
     */
    @PostMapping("/check/accountId")
    @Operation(summary = "App 계정 Id 중복 검사")
    public ResponseDto<AccountIdCheckResponseDto> checkAccountId(
            @Valid @RequestBody AccountIdCheckRequestDto accountIdCheckRequestDto) {

        AccountIdCheckResponseDto accountIdCheckResponseDto =
                signupService.checkAccountId(accountIdCheckRequestDto);

        return ResponseDto.onSuccess(accountIdCheckResponseDto);
    }
}
