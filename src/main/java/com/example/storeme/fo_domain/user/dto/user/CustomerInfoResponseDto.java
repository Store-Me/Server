package com.example.storeme.fo_domain.user.dto.user;

import com.example.storeme.fo_domain.user.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 손님 정보 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CustomerInfoResponseDto {

    private String accountId;

    private String phoneNumber;

    private String profileImageUrl;

    private Boolean hasAppId;

    private Boolean hasKakaoId;
}
