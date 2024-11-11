package com.example.storeme.fo_domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JwtResponseDto {
    private final String accessToken;
    private final String refreshToken;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime expiredTime;
}
