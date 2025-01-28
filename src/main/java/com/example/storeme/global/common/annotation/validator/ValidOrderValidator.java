package com.example.storeme.global.common.annotation.validator;

import com.example.storeme.global.common.annotation.ValidOrder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.IntStream;

public class ValidOrderValidator implements ConstraintValidator<ValidOrder,
        List<?>> {

    private String orderExtractorMethod;

    @Override
    public void initialize(ValidOrder constraintAnnotation) {
        // 애너테이션에서 메서드 이름을 받아옴
        this.orderExtractorMethod = constraintAnnotation.orderExtractorMethod();
    }

    @Override
    public boolean isValid(List<?> value,
                           ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // null이나 비어 있는 경우는 검증하지 않음
        }

        try {
            // 메서드를 동적으로 호출하기 위해 리플렉션 사용
            List<Integer> orders = value.stream()
                    .map(this::getOrderValue)  // 리플렉션으로 메서드 호출
                    .sorted()
                    .toList();

            // 연속적인 0부터 시작하는 값인지 확인
            return IntStream.range(0, orders.size())
                    .allMatch(i -> orders.get(i) == i);
        } catch (Exception e) {
            return false; // 예외 발생 시 검증 실패
        }
    }

    private Integer getOrderValue(Object item) {
        try {
            // 해당 메서드를 리플렉션으로 호출
            Method method = item.getClass().getMethod(orderExtractorMethod);
            return (Integer) method.invoke(item);  // 메서드 실행 후 결과 반환
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract order value", e);
        }
    }
}