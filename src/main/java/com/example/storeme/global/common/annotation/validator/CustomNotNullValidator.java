package com.example.storeme.global.common.annotation.validator;

import com.example.storeme.global.common.annotation.CustomNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.openapitools.jackson.nullable.JsonNullable;

public class CustomNotNullValidator implements ConstraintValidator<CustomNotNull, JsonNullable<?>> {
    @Override
    public boolean isValid(JsonNullable<?> value, ConstraintValidatorContext context) {
        return value == null || !value.isPresent() || (value.isPresent() && value.get() != null);
    }
}