package com.example.storeme.global.common.annotation.validator;

import com.example.storeme.global.common.annotation.CustomPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.regex.Pattern;

public class CustomPatternValidator implements ConstraintValidator<CustomPattern, JsonNullable<String>> {

    private Pattern pattern;

    @Override
    public void initialize(CustomPattern constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regexp());
    }

    @Override
    public boolean isValid(JsonNullable<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isPresent() || value.get()==null ||
                (value.get()!=null && pattern.matcher(value.get()).matches());
    }
}