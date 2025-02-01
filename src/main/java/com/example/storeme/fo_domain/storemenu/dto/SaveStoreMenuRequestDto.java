package com.example.storeme.fo_domain.storemenu.dto;

import com.example.storeme.fo_domain.storemenu.constant.MenuPriceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record SaveStoreMenuRequestDto(
        @NotNull @Positive Long storeId,
        @Positive Long storeMenuCategoryId,
        @NotNull @Length(max = 15) String name,
        @NotNull MenuPriceType priceType,
        @Min(0) Integer fixedPrice,
        @Min(0) Integer rangeMaxPrice,
        @Min(0) Integer rangeMinPrice,
        @Length(max = 50) String description,
        @NotNull Boolean isSignature,
        @NotNull Boolean isPopular,
        @NotNull Boolean isRecommended
) {
}
