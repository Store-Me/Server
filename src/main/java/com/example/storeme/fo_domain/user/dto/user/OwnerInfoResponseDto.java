package com.example.storeme.fo_domain.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사장님 정보 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OwnerInfoResponseDto {

    private String accountId;

    private String storeProfileImageUrl;

    private Boolean hasAppId;

    private Boolean hasKakaoId;
}
