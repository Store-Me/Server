package com.example.storeme.global.common.annotation.validator;

import com.example.storeme.global.common.annotation.CustomLength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.openapitools.jackson.nullable.JsonNullable;

public class CustomLengthValidator implements ConstraintValidator<CustomLength, JsonNullable<String>> {

    private int min;
    private int max;

    @Override
    public void initialize(CustomLength customLength) {
        this.min = customLength.min();
        this.max = customLength.max();
    }

    @Override
    public boolean isValid(JsonNullable<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isPresent() || value.get() == null ||
                (value.get()!=null && value.get().length() >= min && value.get().length() <= max);
    }
}