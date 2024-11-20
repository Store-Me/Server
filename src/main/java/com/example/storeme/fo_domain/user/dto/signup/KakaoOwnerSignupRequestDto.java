package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.fo_domain.user.constant.StoreCategory;
import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * kakao 계정 사장님 타입 회원가입 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class KakaoOwnerSignupRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.KAKAO_ID_REGEX, message = ValidationConstant.KAKAO_ID_MSG)
    private String kakaoId;

    @NotNull
    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String phoneNumber;

    @NotNull
    private Boolean privacyConsent;

    @NotNull
    private Boolean marketingConsent;

    @Pattern(regexp = ValidationConstant.VERIFICATION_CODE_REGEX,
            message = ValidationConstant.VERIFICATION_CODE_MSG)
    private String verificationCode;

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
    private String storeLocationDetail;

    private Double storeLat;

    private Double storeLng;

    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String storePhoneNumber;

    @Length(max = 100)
    private String storeIntro;

}
