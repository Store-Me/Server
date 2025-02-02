package com.example.storeme.global.filter;

import com.example.storeme.global.common.constant.RedisKeyPrefix;
import com.example.storeme.global.common.dto.ReissueJwtResponseDto;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.dto.JwtUserDto;
import com.example.storeme.global.common.exception.JwtAuthenticationException;
import com.example.storeme.global.common.dto.ResponseDto;
import com.example.storeme.global.config.properties.JwtProperties;
import com.example.storeme.global.config.security.UserAuthentication;
import com.example.storeme.global.util.JwtUtil;
import com.example.storeme.global.util.StringRedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final StringRedisUtil stringRedisUtil;
    private final JwtProperties jwtProperties;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // Case 01) Access Token 재발급인 경우(Authorization Header Access Token 유효성 x)
        if (request.getRequestURI().contains("/jwt/reissue")) {
            try {
                Optional<String> accessToken = jwtUtil.extractAccessToken(request);
                Optional<String> refreshToken = jwtUtil.extractRefreshToken(request);
                if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                    throw new JwtAuthenticationException(ErrorStatus._REISSUE_ERROR);
                }
                this.reissueAccessTokenAndRefreshToken(response, accessToken.get(), refreshToken.get());
            } catch (AuthenticationException e) {
                log.warn("Access or Refresh Token 재발급 오류 발생", e);
            }
            finally {
                filterChain.doFilter(request, response);
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

    /**
     * Access & Refresh Token을 재발급하는 메서드
     *
     * 1. refresh token 유효성 검증
     * 2. access token 유효성 검증(유효하지 않아야 함)
     * 3. redis refresh 와 일치 여부 확인
     */
    private void reissueAccessTokenAndRefreshToken(HttpServletResponse response,
                                                   String accessToken, String refreshToken) throws IOException {

        checkAllConditions(accessToken, refreshToken);
        String newAccessToken = jwtUtil.createAccessToken(jwtUtil.getUserInfoFromRefreshToken(refreshToken));
        String newRefreshToken = reIssueRefreshToken(jwtUtil.getUserInfoFromRefreshToken(refreshToken));
        makeAndSendAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);
    }

    /**
     * Access & Refresh Token을 재발급 해야하는 조건인지를 확인하는 메서드
     *
     * 1. access Token 유효하지 않은지 확인
     * 2. refresh Token 유효한지 확인
     * 3. refresh Token 일치하는지 확인
     **/
    private void checkAllConditions(String accessToken, String refreshToken) {

        validateAccessToken(accessToken);
        validateRefreshToken(refreshToken);
        isRefreshTokenMatch(refreshToken);
    }

    /**
     * accessToken이 유효하지 않는지를 확인하는 메서드
     *
     * accessToken이 유효하면 재발급을 하면 안되므로 예외 발생
     */
    private void validateAccessToken(String accessToken) {
        if (jwtUtil.validateToken(accessToken)) {
            log.error("JWT Access Token is valid during the '/reissue' process.");
            throw new JwtAuthenticationException(ErrorStatus._REISSUE_ERROR);
        }
    }

    /**
     * refreshToken이 유효한지를 확인하는 메서드
     *
     * refreshToken이 유효하지 않으면 재발급을 할 수 없으므로 예외 발생
     */
    private void validateRefreshToken(String refreshToken) {
        if (!this.jwtUtil.validateToken(refreshToken)) {
            log.error("JWT Refresh Token is invalid during the '/reissue' process.");
            throw new JwtAuthenticationException(ErrorStatus._REISSUE_ERROR);
        }
    }

    private void isRefreshTokenMatch(String refreshToken) {
        if (!refreshToken.equals(stringRedisUtil.getData(RedisKeyPrefix.REFRESH_TOKEN.getPrefix() +
                jwtUtil.getUserInfoFromRefreshToken(refreshToken).getUserId()))) {
            log.error("JWT Refresh Token is either missing in Redis or does not match the token in Redis.");
            throw new JwtAuthenticationException(ErrorStatus._REISSUE_ERROR);
        }
    }

    /**
     * - refresh token 재발급 하는 메소드
     * 1. 새로운 Refresh Token 발급
     * 2. 해당 Key 에 해당하는 Redis Value 업데이트
     **/
    private String reIssueRefreshToken(JwtUserDto jwtUserDto) {
        stringRedisUtil.deleteData(RedisKeyPrefix.REFRESH_TOKEN.getPrefix() + jwtUserDto.getUserId()); // 기존 refresh token 삭제
        String reIssuedRefreshToken = jwtUtil.createRefreshToken(jwtUserDto);
        stringRedisUtil.setDataExpire(RedisKeyPrefix.REFRESH_TOKEN.getPrefix() + jwtUserDto.getUserId(),
                reIssuedRefreshToken, jwtProperties.getRefreshExpiration()); // refresh token 저장
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
        ReissueJwtResponseDto reissueJwtResponseDto = ReissueJwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredTime(expireTime)
                .build();
        makeResultResponse(response, reissueJwtResponseDto);
    }

    /**
     * 재발급한 토큰이 담긴 응답을 client에게 보내는 메서드
     */
    private void makeResultResponse(HttpServletResponse response,
                                    ReissueJwtResponseDto reissueJwtResponseDto
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.writeValue(os, ResponseDto.onSuccess(reissueJwtResponseDto));
        }
    }

    /**
     * - 일반 API 호출을 처리하는 메소드
     * 1. Authorization 헤더의 access token 검증
     * 2. accessToken 으로부터 JwtUserDto 가져와서 Authentication 객체 생성 및 Security Context에 저장
     **/
    private void checkAccessTokenAndAuthentication(HttpServletRequest request) {
        try {
            // jwt header 에 존재하지 않는 경우
            String accessToken = jwtUtil.extractAccessToken(request)
                    .orElseThrow(() -> {
                        log.error("Access Token is missing in the Authorization header.");
                        return new JwtAuthenticationException(ErrorStatus._UNAUTHORIZED);
                    });

            JwtUserDto jwtUserDto = jwtUtil.getUserInfoFromAccessToken(accessToken);

            // SecurityContext 에 인증된 Authentication 저장
            UserAuthentication authentication = new UserAuthentication(jwtUserDto);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("userId", jwtUserDto.getUserId());

        } catch (AuthenticationException e) {

            log.warn("Access Token 오류 발생", e);
        }
    }
}
