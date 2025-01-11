package com.example.storeme.fo_domain.user.dto.signup;

import com.example.storeme.fo_domain.user.constant.StoreCategory;
import com.example.storeme.fo_domain.user.validator.TrueOnly;
import com.example.storeme.fo_domain.user.validator.ValidStoreNumber;
import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * App 계정 사장님 타입 회원가입 요청 Dto 클래스
 */
@Getter
@NoArgsConstructor
public class AppOwnerSignupRequestDto {

    @NotNull
    @Pattern(regexp = ValidationConstant.ACCOUNT_ID_REGEX, message = ValidationConstant.ACCOUNT_ID_MSG)
    private String accountId;

    @NotNull
    @Pattern(regexp = ValidationConstant.PASSWORD_REGEX, message = ValidationConstant.PASSWORD_MSG)
    private String password;

    @NotNull
    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_REGEX,
            message = ValidationConstant.PHONE_NUMBER_MSG)
    private String phoneNumber;

    @NotNull
    @TrueOnly
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

    @ValidStoreNumber
    private String storePhoneNumber;

    @Length(max = 100)
    private String storeIntro;


}
