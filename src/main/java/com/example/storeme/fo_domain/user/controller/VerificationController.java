package com.example.storeme.fo_domain.user.controller;

import com.example.storeme.fo_domain.user.dto.verification.ConfirmCodeRequestDto;
import com.example.storeme.fo_domain.user.dto.verification.ConfirmCodeResponseDto;
import com.example.storeme.fo_domain.user.dto.verification.VerificationCodeRequestDto;
import com.example.storeme.fo_domain.user.dto.verification.VerificationCodeResponseDto;
import com.example.storeme.fo_domain.user.service.VerificationService;
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
 * 전화번호 인증을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/verification")
@Tag(name = "인증번호 발송 및 유효성 검사")
public class VerificationController {
    private final VerificationService verificationService;

    /**
     * 요청으로 들어온 전화번호로 인증코드를 발송하는 메서드
     */
    @Operation(summary = "인증번호 발송")
    @PostMapping("/send")
    public ResponseDto<VerificationCodeResponseDto> sendVerificationCode(
            @Valid @RequestBody VerificationCodeRequestDto verificationCodeRequestDto) {

        VerificationCodeResponseDto response = verificationService.sendVerificationCode(verificationCodeRequestDto);

        return ResponseDto.onSuccess(response);

    }

    /**
     * 인증번호가 유효한지 확인하는 메서드
     */
    @Operation(summary = "인증번호 유효성 검사")
    @PostMapping("/confirm")
    public ResponseDto<ConfirmCodeResponseDto> confirmVerificationCode(
            @Valid @RequestBody ConfirmCodeRequestDto confirmCodeRequestDto) {

        ConfirmCodeResponseDto confirmCodeResponseDto =
                verificationService.confirmVerificationCode(confirmCodeRequestDto);

        return ResponseDto.onSuccess(confirmCodeResponseDto);
    }
}
