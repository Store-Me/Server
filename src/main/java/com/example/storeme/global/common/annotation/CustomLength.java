package com.example.storeme.global.common.annotation;

import com.example.storeme.global.common.annotation.validator.CustomLengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomLengthValidator.class)  // 유효성 검증 클래스 지정
@Target({ ElementType.FIELD, ElementType.PARAMETER })  // 필드와 파라미터에 적용 가능
@Retention(RetentionPolicy.RUNTIME)  // 런타임 시 유효
public @interface CustomLength {
    String message() default "Length must be between {min} and {max}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 0;  // 최소 길이
    int max() default Integer.MAX_VALUE;  // 최대 길이
}