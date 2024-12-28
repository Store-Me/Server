package com.example.storeme.fo_domain.store.dto;

import com.example.storeme.fo_domain.user.constant.StoreCategory;
import com.example.storeme.global.common.annotation.CustomLength;
import com.example.storeme.global.common.annotation.CustomNotNull;
import com.example.storeme.global.common.annotation.CustomPattern;
import com.example.storeme.global.common.constant.ValidationConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * 가게 정보 수정 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class UpdateStoreInfoRequestDto {

    @CustomNotNull
    private Long storeId;

    @CustomNotNull
    @CustomLength(max = 30)
    private JsonNullable<String> storeName = JsonNullable.undefined();

    private JsonNullable<String> storeDescription = JsonNullable.undefined();

    @CustomNotNull
    private JsonNullable<StoreCategory> storeCategory = JsonNullable.undefined();

    @CustomLength(max = 15)
    private JsonNullable<String> storeDetailCategory = JsonNullable.undefined();

    private JsonNullable<Long> storeFeaturedImageId = JsonNullable.undefined();

    @CustomNotNull
    @CustomLength(max = 10)
    private JsonNullable<String> storeLocation = JsonNullable.undefined();

    @CustomNotNull
    private JsonNullable<Long> storeLocationCode = JsonNullable.undefined();

    @CustomLength(max = 100)
    private JsonNullable<String> storeLocationAddress = JsonNullable.undefined();

    @CustomLength(max = 100)
    private JsonNullable<String> storeLocationDetail = JsonNullable.undefined();

    private JsonNullable<Double> storeLat = JsonNullable.undefined();

    private JsonNullable<Double> storeLng = JsonNullable.undefined();

    @CustomPattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private JsonNullable<String> storePhoneNumber = JsonNullable.undefined();

    @CustomLength(max = 100)
    private JsonNullable<String> storeIntro = JsonNullable.undefined();

    @CustomLength(max = 100)
    private JsonNullable<String> storeNotice = JsonNullable.undefined();
}
