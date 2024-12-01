package com.example.storeme.fo_domain.storeimage.dto;

import com.example.storeme.global.common.annotation.ValidOrder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 가게 이미지 순서 수정 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class UpdateStoreImageOrderRequestDto {

    @NotNull
    private Long storeId;

    @ValidOrder
    List<StoreImageOrderInfoDto> storeImageOrderInfoList;

    @Getter
    public static class StoreImageOrderInfoDto{
        @NotNull
        private Long storeImageId;
        @NotNull
        private Integer storeImageOrder;
    }
}
