package com.example.storeme.fo_domain.user.controller;

import com.example.storeme.fo_domain.user.dto.user.*;
import com.example.storeme.fo_domain.user.service.UserService;
import com.example.storeme.global.common.annotation.AuthInfo;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 관련 요청 처리")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/customer")
    @Operation(summary = "손님 정보 조회")
    public ResponseDto<CustomerInfoResponseDto> getCustomerInfo(@Parameter(hidden = true) @AuthInfo Long userId){

        CustomerInfoResponseDto customerInfoResponseDto = userService.getCustomerInfo(userId);

        return ResponseDto.onSuccess(customerInfoResponseDto);
    }

    @GetMapping("/user/owner")
    @Operation(summary = "사장님 정보 조회")
    public ResponseDto<OwnerInfoResponseDto> getOwnerInfo(@Parameter(hidden = true) @AuthInfo Long userId,
                                      @RequestParam Long storeId){

        OwnerInfoResponseDto ownerInfoResponseDto = userService.getOwnerInfo(userId, storeId);

        return ResponseDto.onSuccess(ownerInfoResponseDto);

    }

    @PatchMapping(value = "/user/customer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "손님 정보 수정")
    public ResponseDto<Void> updateCustomerInfo(@Parameter(hidden = true) @AuthInfo Long userId,
                                                @RequestPart("updateCustomerInfoRequestDto")
                                                @Parameter(
                                                        content = @Content(
                                                                encoding = @Encoding(name = "updateCustomerInfoRequestDto",
                                                                        contentType = MediaType.APPLICATION_JSON_VALUE)))
                                                @Valid
                                                UpdateCustomerInfoRequestDto updateCustomerInfoRequestDto,
                                                @RequestPart(value = "profileImageFile", required = false)
                                                    MultipartFile profileImageFile){

        userService.updateCustomerInfo(userId, updateCustomerInfoRequestDto, profileImageFile);

        return ResponseDto.onSuccess();
    }

    @PatchMapping(value = "/user/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사장님 정보 수정")
    public ResponseDto<Void> updateOwnerInfo(@Parameter(hidden = true) @AuthInfo Long userId,
                                             @RequestPart("updateOwnerInfoRequestDto")
                                             @Parameter(
                                                     content = @Content(
                                                             encoding = @Encoding(name = "updateOwnerInfoRequestDto",
                                                                     contentType = MediaType.APPLICATION_JSON_VALUE)))
                                             @Valid
                                             UpdateOwnerInfoRequestDto updateOwnerInfoRequestDto,
                                             @RequestPart(value = "storeProfileImageFile", required = false)
                                                 MultipartFile storeProfileImageFile){

        userService.updateOwnerInfo(userId, updateOwnerInfoRequestDto, storeProfileImageFile);

        return ResponseDto.onSuccess();
    }

    @PostMapping("/user/customer")
    @Operation(summary = "손님 정보 입력")
    public ResponseDto<Void> saveCustomerInfo(@Parameter(hidden = true) @AuthInfo Long userId,
                                              @RequestPart("saveCustomerInfoRequestDto")
                                              @Parameter(
                                                      content = @Content(
                                                              encoding = @Encoding(name = "saveCustomerInfoRequestDto",
                                                                      contentType = MediaType.APPLICATION_JSON_VALUE)))
                                              @Valid
                                              SaveCustomerInfoRequestDto saveCustomerInfoRequestDto,
                                              @RequestPart(value = "profileImageFile", required = false)
                                                  MultipartFile profileImageFile){
        userService.saveCustomerInfo(userId, saveCustomerInfoRequestDto, profileImageFile);

        return ResponseDto.onSuccess();
    }

    @DeleteMapping("/user")
    @Operation(summary = "회원탈퇴")
    public ResponseDto<Void> deleteUser(@Parameter(hidden = true) @AuthInfo Long userId){

        userService.deleteUser(userId);

        return ResponseDto.onSuccess();
    }

}
