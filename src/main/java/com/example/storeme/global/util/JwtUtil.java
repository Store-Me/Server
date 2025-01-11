package com.example.storeme.global.util;

import com.example.storeme.fo_domain.user.constant.RoleType;
import com.example.storeme.fo_domain.user.dto.user.JwtResponseDto;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.constant.RedisKeyPrefix;
import com.example.storeme.global.common.dto.JwtUserDto;
import com.example.storeme.global.common.exception.JwtAuthenticationException;
import com.example.storeme.global.config.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final StringRedisUtil stringRedisUtil;

    // HttpServletRequest 부터 Access Token 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getAccessHeader()))
                .filter(StringUtils::hasText)
                .filter(accessToken -> accessToken.startsWith(jwtProperties.getBearer()))
                .map(accessToken -> accessToken.substring(jwtProperties.getBearer().length()+1));
    }

    // HttpServletRequest 부터 Refresh Token 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getRefreshHeader()));
    }

    // access token 생성
    public String createAccessToken(JwtUserDto jwtUserDto) {
        return this.createToken(jwtUserDto, jwtProperties.getAccessExpiration());
    }

    // refresh token 생성
    public String createRefreshToken(JwtUserDto jwtUserDto) {
        return this.createToken(jwtUserDto, jwtProperties.getRefreshExpiration());

    }
    // access token 으로부터 JwtUserDto 객체 반환
    public JwtUserDto getUserInfoFromAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String roleType = claims.get("roleType", String.class);

            return JwtUserDto.builder()
                    .userId(claims.get("userId", String.class))
                    .roleType(RoleType.valueOf(roleType))
                    .build();

        } catch (Exception exception) {
            log.error("Access Token is invalid.", exception);
            throw new JwtAuthenticationException(ErrorStatus._UNAUTHORIZED);
        }
    }
    // refresh token 으로부터 JwtUserDto 객체 반환
    public JwtUserDto getUserInfoFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return JwtUserDto.builder()
                    .userId(claims.get("userId", String.class))
                    .roleType(claims.get("roleType", RoleType.class))
                    .build();
        } catch (Exception exception) {
            log.error("JWT Refresh Token is invalid during the '/reissue' process.");
            throw new JwtAuthenticationException(ErrorStatus._REISSUE_ERROR);
        }
    }

    // kakao oauth 로그인 & 일반 로그인 시 jwt 응답 생성 + redis refresh 저장
    public JwtResponseDto createJwtResponse(JwtUserDto jwtUserDto) {
        stringRedisUtil.deleteData(RedisKeyPrefix.REFRESH_TOKEN.getPrefix() + jwtUserDto.getUserId());
        String accessToken = createAccessToken(jwtUserDto);
        String refreshToken = createRefreshToken(jwtUserDto);

        /* 서비스 토큰 생성 */
        JwtResponseDto jwtResponseDto = JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredTime(LocalDateTime.now().plusSeconds(jwtProperties.getAccessExpiration() / 1000))
                .build();

        /* redis refresh token 저장 */
        stringRedisUtil.setDataExpire(RedisKeyPrefix.REFRESH_TOKEN.getPrefix() + jwtUserDto.getUserId(),
                jwtResponseDto.getRefreshToken(), jwtProperties.getRefreshExpiration());

        return jwtResponseDto;
    }
    // token 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            log.warn("만료된 jwt 입니다.");
        } catch (UnsupportedJwtException exception) {
            log.warn("지원되지 않는 jwt 입니다.");
        } catch (IllegalArgumentException exception) {
            log.warn("token에 값이 없습니다.");
        } catch(SecurityException  exception){
            log.warn("signature에 오류가 존재합니다.");
        } catch(MalformedJwtException exception){
            log.warn("jwt가 유효하지 않습니다.");
        }
        return false;
    }

    // JWT Token 생성 로직
    private String createToken(JwtUserDto jwtUserDto, Long tokenExpiration) {
        Claims claims = Jwts.claims();
        claims.put("userId", jwtUserDto.getUserId());
        claims.put("roleType", jwtUserDto.getRoleType());
        Date tokenExpiresIn = new Date(new Date().getTime() + tokenExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(tokenExpiresIn)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }
}
