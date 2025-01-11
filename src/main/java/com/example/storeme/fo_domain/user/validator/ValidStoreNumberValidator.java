package com.example.storeme.fo_domain.user.validator;

import static com.example.storeme.global.common.constant.ValidationConstant.INTERNET_PHONE_PATTERN;
import static com.example.storeme.global.common.constant.ValidationConstant.LANDLINE_PHONE_PATTERN;
import static com.example.storeme.global.common.constant.ValidationConstant.MOBILE_PHONE_PATTERN;
import static com.example.storeme.global.common.constant.ValidationConstant.SAFE_NUMBER_PATTERN;

import com.example.storeme.global.common.constant.AreaCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidStoreNumberValidator implements ConstraintValidator<ValidStoreNumber, String> {

    // 지역번호를 정의
    private static final Set<String> areaCodes = Set.of(
            "02", "032", "042", "051", "052", "053", "062", "064", "031", "033", "041", "043", "054", "055", "061", "063"
    );

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {

        // 안심번호 검증
        if (Pattern.matches(SAFE_NUMBER_PATTERN, input)) {
            return true;
        }

        // 휴대전화번호 검증
        else if (Pattern.matches(MOBILE_PHONE_PATTERN, input)) {
            return true;
        }

        // 인터넷전화번호 검증
        else if (Pattern.matches(INTERNET_PHONE_PATTERN, input)) {
            return true;
        }

        // 유선전화번호 검증
        else {
            for (AreaCode areaCode : AreaCode.values()) {
                if (Pattern.matches("^" + areaCode.getCode() + LANDLINE_PHONE_PATTERN, input)) {
                    return true;
                }
            }
        }

        return false;
    }
}