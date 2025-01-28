package com.example.storeme.fo_domain.storemenu.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record StoreMenuCategoryListResponseDto(
        List<StoreMenuCategoryInfoDto> storeMenuCategoryInfoList
) {
    public record StoreMenuCategoryInfoDto(
            Long id,
            String category,
            Integer order
    ) {
    }
}
