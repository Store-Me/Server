package com.example.storeme.fo_domain.storeimage.annotation;

import com.example.storeme.fo_domain.storeimage.annotation.validator.ValidOrderValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidOrderValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrder {
    String message() default "Order values must start from 0 and be consecutive.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}