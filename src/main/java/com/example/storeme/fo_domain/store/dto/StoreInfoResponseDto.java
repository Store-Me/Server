package com.example.storeme.fo_domain.store.dto;
import com.example.storeme.fo_domain.user.constant.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 가게 정보 조회 요청 응답 Dto 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StoreInfoResponseDto {

    private String storeName;

    private String storeProfileImageUrl;

    private String storeFeaturedImageUrl;

    private List<StoreImageDto> storeImageDtoList;

    private String storeBannerImageUrl;

    private String storeDescription;

    private StoreCategory storeCategory;

    private String storeDetailCategory;

    private String storeLocation;

    private Long storeLocationCode;

    private String storeLocationDetail;

    private Double storeLat;

    private Double storeLng;

    private String storePhoneNumber;

    private String storeNotice;

    private String storeIntro;

    @Getter
    @Builder
    public static class StoreImageDto {
        private Long storeImageId;
        private String storeImageUrl;
        private Integer order;
    }
}
