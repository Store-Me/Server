package com.example.storeme.fo_domain.user.service;

import com.example.storeme.fo_domain.user.constant.RoleType;
import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.dto.signup.*;
import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.constant.RedisKeyPrefix;
import com.example.storeme.global.util.StringRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 관련 기능을 처리하는 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {
    private final VerificationService verificationService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StringRedisUtil stringRedisUtil;

    /**
     * App 계정 회원가입 요청을 처리하는 메서드
     */
    public void handleAppSignup(AppSignupRequestDto appSignupRequestDto) {

        // 인증 코드가 유효한지 검사
        verificationService.checkIfVerificationCodeConfirmed(appSignupRequestDto.getPhoneNumber(),
                appSignupRequestDto.getVerificationCode());

        // 유효성 검사가 끝난 인증 코드를 redis에서 삭제
        stringRedisUtil.deleteData(appSignupRequestDto.getPhoneNumber());

        userRepository.save(User.builder()
                .accountId(appSignupRequestDto.getAccountId())
                .password(bCryptPasswordEncoder.encode(appSignupRequestDto.getPassword()))
                .phoneNumber(appSignupRequestDto.getPhoneNumber())
                .nickname(appSignupRequestDto.getNickname())
                .profileImageUrl(appSignupRequestDto.getProfileImageUrl())
                .privacyConsent(appSignupRequestDto.getPrivacyConsent())
                .marketingConsent(appSignupRequestDto.getMarketingConsent())
                .roleType(RoleType.ROLE_USER)
                .build());
    }

    /**
     * Kakao 계정 회원가입 요청을 처리하는 메서드
     */
    public void handleKakaoSignup(KakaoSignupRequestDto kakaoSignupRequestDto) {

        // 인증 코드가 유효한지 검사
        verificationService.checkIfVerificationCodeConfirmed(kakaoSignupRequestDto.getPhoneNumber(),
                kakaoSignupRequestDto.getVerificationCode());

        // 유효성 검사가 끝난 인증 코드를 redis에서 삭제
        stringRedisUtil.deleteData(kakaoSignupRequestDto.getPhoneNumber());

        userRepository.save(User.builder()
                .kakaoId(kakaoSignupRequestDto.getKakaoId())
                .phoneNumber(kakaoSignupRequestDto.getPhoneNumber())
                .nickname(kakaoSignupRequestDto.getNickname())
                .profileImageUrl(kakaoSignupRequestDto.getProfileImageUrl())
                .privacyConsent(kakaoSignupRequestDto.getPrivacyConsent())
                .marketingConsent(kakaoSignupRequestDto.getMarketingConsent())
                .roleType(RoleType.ROLE_USER)
                .build());
    }

    /**
     * App 계정 연동 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleAppLinkSignup(AppLinkSignupRequestDto appLinkSignupRequestDto) {

        // 인증 코드가 유효한지 검사
        verificationService.checkIfVerificationCodeConfirmed(appLinkSignupRequestDto.getPhoneNumber(),
                appLinkSignupRequestDto.getVerificationCode());

        // 유효성 검사가 끝난 인증 코드를 redis에서 삭제
        stringRedisUtil.deleteData(appLinkSignupRequestDto.getPhoneNumber());

        User user = userRepository.findByPhoneNumber(appLinkSignupRequestDto.getPhoneNumber()).orElseThrow(() -> {
                    log.error("User not found with phone number: {}", appLinkSignupRequestDto.getPhoneNumber());
                    return new UserException(ErrorStatus._BAD_REQUEST);
                }
        );

        user.setAccountId(appLinkSignupRequestDto.getAccountId());
        user.setPassword(bCryptPasswordEncoder.encode(appLinkSignupRequestDto.getPassword()));
    }

    /**
     * App 계정 연동 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleKakaoLinkSignup(KakaoLinkSignupRequestDto kakaoLinkSignupRequestDto) {

        // 인증 코드가 유효한지 검사
        verificationService.checkIfVerificationCodeConfirmed(kakaoLinkSignupRequestDto.getPhoneNumber(),
                kakaoLinkSignupRequestDto.getVerificationCode());

        // 유효성 검사가 끝난 인증 코드를 redis에서 삭제
        stringRedisUtil.deleteData(kakaoLinkSignupRequestDto.getPhoneNumber());

        User user = userRepository.findByPhoneNumber(kakaoLinkSignupRequestDto.getPhoneNumber()).orElseThrow(() -> {
                    log.error("User not found with phone number: {}", kakaoLinkSignupRequestDto.getPhoneNumber());
                    return new UserException(ErrorStatus._BAD_REQUEST);
                }
        );

        user.setKakaoId(kakaoLinkSignupRequestDto.getKakaoId());
    }

    /**
     * App 계정 Id 중복검사 요청을 처리하는 메서드
     */
    @Transactional(readOnly = true)
    public AccountIdCheckResponseDto checkAccountId(AccountIdCheckRequestDto accountIdCheckRequestDto){

        boolean isDuplicated = userRepository.existsByAccountId(accountIdCheckRequestDto.getAccountId());

        return AccountIdCheckResponseDto.builder()
                .isDuplicated(isDuplicated)
                .build();
    }

}

