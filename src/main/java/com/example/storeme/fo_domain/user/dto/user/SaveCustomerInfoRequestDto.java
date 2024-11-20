package com.example.storeme.fo_domain.user.dto.user;

import com.example.storeme.global.common.constant.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 손님 정보 입력 요청 Dto 클래스
 */
@NoArgsConstructor
@Getter
public class SaveCustomerInfoRequestDto {

    @NotNull
    @Length(max = 10, message = ValidationConstant.NICKNAME_MSG)
    private String nickname;
}
