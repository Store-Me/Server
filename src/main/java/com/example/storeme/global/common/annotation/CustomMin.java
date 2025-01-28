package com.example.storeme.global.common.annotation;

import com.example.storeme.global.common.annotation.validator.CustomMinValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomMinValidator.class)  // CustomMinValidator가 실제 검증 로직을 수행
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomMin {

    String message() default "Value is below the minimum allowed";  // 기본 메시지

    Class<?>[] groups() default {};  // 그룹화

    Class<? extends Payload>[] payload() default {};  // 페이로드

    int value();  // 최소값을 설정하는 속성
}