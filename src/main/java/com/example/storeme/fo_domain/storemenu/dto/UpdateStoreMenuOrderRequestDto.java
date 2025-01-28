package com.example.storeme.fo_domain.storemenu.dto;

import com.example.storeme.global.common.annotation.ValidOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record UpdateStoreMenuOrderRequestDto(
        @NotNull @Positive Long storeId,
        @NotNull @Positive Long storeMenuCategoryId,
        @ValidOrder(orderExtractorMethod = "getOrder")
        List<StoreMenuOrderInfoDto> storeMenuOrderInfoList

) {
    public record StoreMenuOrderInfoDto(
            @NotNull @Positive Long storeMenuId,
            @NotNull @Positive Integer order
    ){
    }
}
