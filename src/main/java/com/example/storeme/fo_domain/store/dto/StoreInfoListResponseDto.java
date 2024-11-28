package com.example.storeme.fo_domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 유저가 관리하는 가게 정보 리스트 조회 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StoreInfoListResponseDto {
    private List<StoreInfoDto> storeInfoList;

    @Getter
    @AllArgsConstructor
    public static class StoreInfoDto {
        private Long storeId;

        private String storeName;

        private String storeProfileImageUrl;
    }
}
