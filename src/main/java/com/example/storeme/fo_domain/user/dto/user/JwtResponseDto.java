package com.example.storeme.fo_domain.user.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 로그인 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime expiredTime;
}
