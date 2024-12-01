package com.example.storeme.fo_domain.storemenu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 가게 메뉴 카테고리 이름 수정 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class UpdateStoreMenuCategoryNameRequestDto {

    @NotNull
    private Long storeId;

    @NotNull
    private Long storeMenuCategoryId;

    @NotNull
    @Length(max = 20)
    private String storeMenuCategory;
}
