package com.example.storeme.fo_domain.user.controller;

import com.example.storeme.fo_domain.user.dto.signup.*;
import com.example.storeme.fo_domain.user.service.SignupService;
import com.example.storeme.global.common.annotation.SwaggerBody;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * App 계정 손님타입 회원가입 요청을 처리하는 메서드
     */
    @Operation(summary = "App 계정 손님 타입 회원가입")
    @SwaggerBody(content = @Content(
            encoding = @Encoding(name = "appCustomerSignupRequestDto", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(value = "/app/customer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Void> handleAppCustomerSignup(
            @RequestPart("appCustomerSignupRequestDto") @Valid AppCustomerSignupRequestDto appCustomerSignupRequestDto,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        signupService.handleAppCustomerSignup(appCustomerSignupRequestDto, profileImageFile);
        return ResponseDto.onSuccess();
    }

    /**
     * App 계정 사장님 타입 회원가입 요청을 처리하는 메서드
     */
    @Operation(summary = "App 계정 사장님 타입 회원가입")
    @SwaggerBody(content = @Content(
            encoding = @Encoding(name = "appOwnerSignupRequestDto", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(value = "/app/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Void> handleAppOwnerSignup(
            @RequestPart @Valid AppOwnerSignupRequestDto appOwnerSignupRequestDto,
            @RequestPart(value = "storeProfileImageFile", required = false) MultipartFile storeProfileImageFile,
            @RequestPart(value = "storeFeaturedImageFile", required = false) MultipartFile storeFeaturedImageFile,
            @RequestPart(value = "storeImageFileList", required = false) List<MultipartFile> storeImageFileList) {
        signupService.handleAppOwnerSignup(appOwnerSignupRequestDto, storeProfileImageFile,
                storeFeaturedImageFile, storeImageFileList);
        return ResponseDto.onSuccess();
    }

    /**
     * Kakao 계정 손님타입 회원가입 요청을 처리하는 메서드
     */
    @PostMapping(value = "/kakao/customer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SwaggerBody(content = @Content(
            encoding = @Encoding(name = "kakaoCustomerSignupRequestDto", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @Operation(summary = "Kakao 계정 손님타입 회원가입")
    public ResponseDto<Void> handleKakaoCustomerSignup(
            @RequestPart @Valid KakaoCustomerSignupRequestDto kakaoCustomerSignupRequestDto,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        signupService.handleKakaoCustomerSignup(kakaoCustomerSignupRequestDto, profileImageFile);
        return ResponseDto.onSuccess();
    }

    /**
     * Kakao 계정 사장님 타입 회원가입 요청을 처리하는 메서드
     */
    @Operation(summary = "Kakao 계정 사장님 타입 회원가입")
    @SwaggerBody(content = @Content(
            encoding = @Encoding(name = "kakaoOwnerSignupRequestDto", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(value = "/kakao/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Void> handleKakaoOwnerSignup(
            @RequestPart @Valid KakaoOwnerSignupRequestDto kakaoOwnerSignupRequestDto,
            @RequestPart(value = "storeProfileImageFile", required = false) MultipartFile storeProfileImageFile,
            @RequestPart(value = "storeFeaturedImageFile", required = false) MultipartFile storeFeaturedImageFile,
            @RequestPart(value = "storeImageFileList", required = false) List<MultipartFile> storeImageFileList) {
        signupService.handleKakaoOwnerSignup(kakaoOwnerSignupRequestDto, storeProfileImageFile,
                storeFeaturedImageFile, storeImageFileList);
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
     * 회원가입 모드 조회 요청을 처리하는 메서드
     */
    @GetMapping("/signup/mode")
    @Operation(summary = "회원가입 모드 조회")
    public ResponseDto<SignupModeResponseDto> getSignupMode(
            @Valid @RequestBody SignupModeRequestDto signupModeRequestDto) {
        SignupModeResponseDto signupModeResponseDto = signupService.getSignupMode(signupModeRequestDto);

        return ResponseDto.onSuccess(signupModeResponseDto);
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
