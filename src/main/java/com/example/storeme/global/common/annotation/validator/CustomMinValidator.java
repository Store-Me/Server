package com.example.storeme.global.common.annotation.validator;

import com.example.storeme.global.common.annotation.CustomMin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.openapitools.jackson.nullable.JsonNullable;

public class CustomMinValidator implements ConstraintValidator<CustomMin, JsonNullable<Integer>> {

    private int min;

    @Override
    public void initialize(CustomMin customMin) {
        this.min = customMin.value();  // 애노테이션에서 설정한 최소값을 가져옴
    }

    @Override
    public boolean isValid(JsonNullable<Integer> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // JsonNullable이 null일 경우 검증을 하지 않음
        }

        // 값이 없으면 유효하다고 간주
        if (!value.isPresent()) {
            return true;
        }

        Integer valueContent = value.get();  // 값이 존재할 경우 값을 가져옴

        // 값이 null이면 유효하지 않음
        if (valueContent == null) {
            return false;
        }

        // 최소값보다 작은지 체크
        return valueContent >= min;
    }
}