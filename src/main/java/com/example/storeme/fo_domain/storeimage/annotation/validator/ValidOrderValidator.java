package com.example.storeme.fo_domain.storeimage.annotation.validator;

import com.example.storeme.fo_domain.storeimage.annotation.ValidOrder;
import com.example.storeme.fo_domain.storeimage.dto.UpdateStoreImageOrderRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.IntStream;

public class ValidOrderValidator implements ConstraintValidator<ValidOrder,
        List<UpdateStoreImageOrderRequestDto.StoreImageOrderInfoDto>> {

    @Override
    public boolean isValid(List<UpdateStoreImageOrderRequestDto.StoreImageOrderInfoDto> value,
                           ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // null이나 비어 있는 경우는 검증하지 않음
        }

        List<Integer> orders = value.stream()
                .map(UpdateStoreImageOrderRequestDto.StoreImageOrderInfoDto::getStoreImageOrder)
                .sorted()
                .toList();

        // 연속적인 0부터 시작하는 값인지 확인
        return IntStream.range(0, orders.size())
                .allMatch(i -> orders.get(i) == i);
    }
}