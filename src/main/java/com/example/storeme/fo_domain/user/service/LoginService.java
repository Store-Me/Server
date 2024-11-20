package com.example.storeme.fo_domain.user.service;

import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.dto.user.AppLoginRequestDto;
import com.example.storeme.fo_domain.user.dto.user.JwtResponseDto;
import com.example.storeme.fo_domain.user.dto.user.KakaoLoginRequestDto;
import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.dto.JwtUserDto;
import com.example.storeme.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 기능을 처리하는 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * App 계정 로그인을 처리하는 메서드
     */
    @Transactional(readOnly = true)
    public JwtResponseDto handleAppLogin(AppLoginRequestDto appLoginRequestDto){

        User user = userRepository.findByAccountId(appLoginRequestDto.getAccountId()).orElseThrow(() -> {
            log.error("User not found with accountId: {}", appLoginRequestDto.getAccountId());
            return new UserException(ErrorStatus._BAD_REQUEST);
        });

        // 비밀번호가 일치하는지 확인
        if(!bCryptPasswordEncoder.matches(appLoginRequestDto.getPassword(), user.getPassword())){
            log.error("Wrong password");
            throw new UserException(ErrorStatus._BAD_REQUEST);
        }

        return jwtUtil.createJwtResponse(JwtUserDto.builder()
                .userId(String.valueOf(user.getId()))
                .roleType(user.getRoleType())
                .build());
    }



    /**
     * Kakao 계정 로그인을 처리하는 메서드
     */
    @Transactional(readOnly = true)
    public JwtResponseDto handleKakaoLogin(KakaoLoginRequestDto kakaoLoginRequestDto){

        User user = userRepository.findByKakaoId(kakaoLoginRequestDto.getKakaoId()).orElseThrow(() -> {
            log.error("User not found with kakaoId: {}", kakaoLoginRequestDto.getKakaoId());
            return new UserException(ErrorStatus._BAD_REQUEST);
        });

        return jwtUtil.createJwtResponse(JwtUserDto.builder()
                .userId(String.valueOf(user.getId()))
                .roleType(user.getRoleType())
                .build());

    }
}
