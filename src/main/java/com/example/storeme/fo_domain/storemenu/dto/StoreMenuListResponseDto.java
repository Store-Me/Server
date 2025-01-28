package com.example.storeme.fo_domain.storemenu.dto;

import com.example.storeme.fo_domain.storemenu.constant.MenuPriceType;
import com.example.storeme.fo_domain.storemenu.domain.StoreMenu;
import java.util.List;
import lombok.Builder;

@Builder
public record StoreMenuListResponseDto(
        List<StoreMenuInfoDto> storeMenuInfoList
) {
    @Builder
    public record StoreMenuInfoDto(
            Long id,
            String name,
            Integer order,
            MenuPriceType priceType,
            Integer fixedPrice,
            Integer rangeMaxPrice,
            Integer rangeMinPrice,
            String imageUrl,
            String description,
            Boolean isSignature,
            Boolean isPopular,
            Boolean isRecommended
    ) {
    }

    public static StoreMenuInfoDto toDto(StoreMenu storeMenu) {
        return StoreMenuInfoDto.builder()
                .id(storeMenu.getId())
                .name(storeMenu.getName())
                .order(storeMenu.getOrder())
                .priceType(storeMenu.getPriceType())
                .fixedPrice(storeMenu.getFixedPrice())
                .rangeMaxPrice(storeMenu.getRangeMaxPrice())
                .rangeMinPrice(storeMenu.getRangeMinPrice())
                .imageUrl(storeMenu.getImageUrl())
                .description(storeMenu.getDescription())
                .isSignature(storeMenu.getIsSignature())
                .isPopular(storeMenu.getIsPopular())
                .isRecommended(storeMenu.getIsRecommended())
                .build();
    }
}
