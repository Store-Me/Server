package com.example.storeme.fo_domain.storemenu.dto;

import com.example.storeme.fo_domain.storemenu.constant.MenuPriceType;
import com.example.storeme.global.common.annotation.CustomLength;
import com.example.storeme.global.common.annotation.CustomMin;
import com.example.storeme.global.common.annotation.CustomNotNull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateStoreMenuRequestDto(
        @NotNull @Positive Long storeId,
        @NotNull @Positive Long storeMenuCategoryId,
        @NotNull @Positive Long storeMenuId,
        @CustomLength(max = 15) JsonNullable<String> name,
        @CustomNotNull JsonNullable<MenuPriceType> priceType,
        @CustomMin(0) JsonNullable<Integer> fixedPrice,
        @CustomMin(0) JsonNullable<Integer> rangeMaxPrice,
        @CustomMin(0) JsonNullable<Integer> rangeMinPrice,
        @CustomLength(max = 50) JsonNullable<String> description,
        @CustomNotNull JsonNullable<Boolean> isSignature,
        @CustomNotNull JsonNullable<Boolean> isPopular,
        @CustomNotNull JsonNullable<Boolean> isRecommended
) {
    public UpdateStoreMenuRequestDto {
        name = JsonNullable.undefined();
        priceType = JsonNullable.undefined();
        fixedPrice = JsonNullable.undefined();
        rangeMaxPrice = JsonNullable.undefined();
        rangeMinPrice = JsonNullable.undefined();
        description = JsonNullable.undefined();
        isSignature = JsonNullable.undefined();
        isPopular = JsonNullable.undefined();
        isRecommended = JsonNullable.undefined();
    }
}
