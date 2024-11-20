package com.example.storeme.fo_domain.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrueOnlyValidator implements ConstraintValidator<TrueOnly, Boolean> {

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return value;
    }
}
