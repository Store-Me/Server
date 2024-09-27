package com.example.storeme.fo_domain.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReissueTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime expiredTime;
}
