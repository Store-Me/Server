package com.example.storeme.fo_domain.store.dto;

import com.example.storeme.fo_domain.user.constant.StoreCategory;
import com.example.storeme.fo_domain.user.validator.ValidStoreNumber;
import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 가게 정보 저장 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class SaveStoreInfoRequestDto {
    @NotNull
    @Length(max = 30)
    private String storeName;

    private String storeDescription;

    @NotNull
    private StoreCategory storeCategory;

    @Length(max = 15)
    private String storeDetailCategory;

    @NotNull
    @Length(max = 10)
    private String storeLocation;

    @NotNull
    private Long storeLocationCode;

    @Length(max = 100)
    private String storeLocationAddress;

    @Length(max = 100)
    private String storeLocationDetail;

    private Double storeLat;

    private Double storeLng;

    @ValidStoreNumber
    private String storePhoneNumber;

    @Length(max = 100)
    private String storeIntro;

    @Length(max = 100)
    private String storeNotice;
}
