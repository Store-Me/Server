package com.example.storeme.global.filter;

import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.dto.ReissueTokenResponseDto;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.exception.JwtAuthenticationException;
import com.example.storeme.global.common.response.ResponseDto;
import com.example.storeme.global.config.properties.JwtProperties;
import com.example.storeme.global.config.security.UserAuthentication;
import com.example.storeme.global.util.JwtUtil;
import com.example.storeme.global.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // Case 01) Access Token 재발급인 경우(Authorization Header Access Token 유효성 x)
        if (request.getRequestURI().contains("/reissue")) {
            try {
                Optional<String> accessToken = jwtUtil.extractAccessToken(request);
                Optional<String> refreshToken = jwtUtil.extractRefreshToken(request);
                if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                    throw new JwtAuthenticationException(ErrorStatus._JWT_IS_NOT_EXIST);
                }
                this.reissueAccessTokenAndRefreshToken(response, accessToken.get(), refreshToken.get());
            } catch (Exception e) {
                log.warn("Access or Refresh Token 재발급 오류 발생", e);

                if(e instanceof  JwtAuthenticationException)
                    request.setAttribute("exception", ((JwtAuthenticationException) e).getCode());
                else
                    request.setAttribute("exception", ErrorStatus._INTERNAL_SERVER_ERROR);

            }
        }
        // Case 02) 일반 API 요청인 경우
        else {
            checkAccessTokenAndAuthentication(request);
            log.info("jwtAuthentication filter is finished");
            // Authentication Exception 없이 정상 인증처리 된 경우
            // 기존 필터 체인 호출
            filterChain.doFilter(request, response);
        }

    }

    private void reissueAccessTokenAndRefreshToken(HttpServletResponse response,
                                                   String accessToken, String refreshToken) throws AuthenticationException, IOException {
        /**
         * 1. refresh token 유효성 검증
         * 2. access token 유효성 검증(유효하지 않아야 함)
         * 3. redis refresh 와 일치 여부 확인
         */
        checkAllConditions(accessToken, refreshToken);
        String newAccessToken = jwtUtil.createAccessToken(jwtUtil.getUserIdFromRefreshToken(refreshToken));
        String newRefreshToken = reIssueRefreshToken(jwtUtil.getUserIdFromRefreshToken(refreshToken));
        makeAndSendAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);
    }

    // Access Token + Refresh Token 재발급 메소드
    private void checkAllConditions(String accessToken, String refreshToken) {
        /**
         * 1. access Token 유효하지 않은지 확인
         * 2. refresh Token 유효한지 확인
         * 3. refresh Token 일치하는지 확인
         **/
        validateAccessToken(accessToken);
        validateRefreshToken(refreshToken);
        isRefreshTokenMatch(refreshToken);
    }

    private void validateAccessToken(String accessToken) {
        if (jwtUtil.validateToken(accessToken)) {
            throw new JwtAuthenticationException(ErrorStatus._JWT_ACCESS_TOKEN_IS_VALID);
        }
    }

    private void validateRefreshToken(String refreshToken) {
        if (!this.jwtUtil.validateToken(refreshToken)) {
            throw new JwtAuthenticationException(ErrorStatus._JWT_REFRESH_TOKEN_IS_NOT_VALID);
        }
    }

    private void isRefreshTokenMatch(String refreshToken) {
        if (!refreshToken.equals(redisUtil.getData(jwtUtil.getUserIdFromRefreshToken(refreshToken)))) {
            throw new JwtAuthenticationException(ErrorStatus._JWT_REFRESH_TOKEN_IS_NOT_MATCH);
        }
    }

    /**
     * - refresh token 재발급 하는 메소드
     * 1. 새로운 Refresh Token 발급
     * 2. 해당 Key 에 해당하는 Redis Value 업데이트
     **/
    private String reIssueRefreshToken(String userId) {
        redisUtil.deleteData(userId); // 기존 refresh token 삭제
        String reIssuedRefreshToken = jwtUtil.createRefreshToken(userId);
        redisUtil.setDataExpire(userId, reIssuedRefreshToken, jwtProperties.getRefreshExpiration()); // refresh token 저장
        return reIssuedRefreshToken;
    }

    /**
     * - 재 발급한 refresh & access token 응답으로 보내는 메소드
     * 1. 상태 코드 설정
     * 2. 응답 헤더에 설정 (jwtProperties 에서 정보 가져옴)
     **/
    private void makeAndSendAccessTokenAndRefreshToken(HttpServletResponse response,
                                                       String accessToken,
                                                       String refreshToken) throws IOException {
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(this.jwtProperties.getAccessExpiration() / 1000);
        // refresh token, access token 을 응답 본문에 넣어 응답
        ReissueTokenResponseDto reissueTokenResponseDto = ReissueTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredTime(expireTime)
                .build();
        makeResultResponse(response, reissueTokenResponseDto);
    }

    private void makeResultResponse(HttpServletResponse response,
                                    ReissueTokenResponseDto reissueTokenResponseDto
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.writeValue(os, ResponseDto.onSuccess(reissueTokenResponseDto));
        }
    }

    /**
     * - 일반 API 호출을 처리하는 메소드
     * 1. Authorization 헤더의 access token 검증
     * 2. accessToken 으로부터 UserId 가져와서 userRepository 조회
     * 3. Authentication 객체 생성 및 Security Context에 저장
     **/
    private void checkAccessTokenAndAuthentication(HttpServletRequest request) {
        try {
            // jwt header 에 존재하지 않는 경우
            String accessToken = jwtUtil.extractAccessToken(request)
                    .orElseThrow(() -> new JwtAuthenticationException(ErrorStatus._JWT_IS_NOT_EXIST));

            Long userId = Long.valueOf(jwtUtil.getUserIdFromAccessToken(accessToken));
            // accessToken 을 통해 User Payload 가져 오고 회원 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new JwtAuthenticationException(ErrorStatus._USER_NOT_FOUND));

            // SecurityContext 에 인증된 Authentication 저장
            UserAuthentication authentication = new UserAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("UserId", userId);

        } catch (AuthenticationException e) {
            request.setAttribute("exception", e.getMessage());
            log.warn("Access Token 오류 발생", e);
        }
    }
}
