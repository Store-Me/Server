package com.example.storeme.fo_domain.storemenu.dto;

import com.example.storeme.global.common.annotation.ValidOrder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 가게 메뉴 카테고리 순서 수정 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class UpdateStoreMenuCategoryOrderRequestDto {
    @NotNull
    private Long storeId;

    @ValidOrder(orderExtractorMethod = "getStoreMenuCategoryOrder")
    List<StoreMenuCategoryOrderInfoDto> storeMenuCategoryOrderInfoList;

    @Getter
    public static class StoreMenuCategoryOrderInfoDto{
        @NotNull
        private Long storeMenuCategoryId;
        @NotNull
        private Integer storeMenuCategoryOrder;
    }
}
