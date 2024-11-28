package com.example.storeme.global.common.annotation;

import com.example.storeme.global.common.annotation.validator.CustomNotNullValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomNotNullValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomNotNull {
    String message() default "Value must not be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}