package com.example.storeme.global.common.annotation;

import com.example.storeme.global.common.annotation.validator.CustomStoreNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomStoreNumberValidator.class) // 검증기 클래스 지정
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // 필드나 파라미터에 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임 동안 유지
public @interface CustomStoreNumber {
    String message() default "유효하지 않은 가게 전화번호입니다."; // 기본 오류 메시지
    Class<?>[] groups() default {}; // 그룹화
    Class<? extends Payload>[] payload() default {}; // 추가 메타데이터
}
