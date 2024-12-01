package com.example.storeme.fo_domain.storemenu.controller;

import com.example.storeme.fo_domain.storemenu.dto.SaveStoreMenuCategoryRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuCategoryNameRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuCategoryOrderRequestDto;
import com.example.storeme.fo_domain.storemenu.service.StoreMenuCategoryService;
import com.example.storeme.global.common.annotation.AuthInfo;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 메뉴 카테고리 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/store/menu/category")
@Tag(name = "가게 메뉴 카테고리 관련 요청 처리")
public class StoreMenuCategoryController {

    private final StoreMenuCategoryService storeMenuCategoryService;

    /**
     * 가게 메뉴 카테고리 저장 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 카테고리 저장")
    @PostMapping
    public ResponseDto<Void> saveStoreMenuCategory(@Parameter(hidden = true) @AuthInfo Long userId,
                                                                         @RequestBody
                                                                         @Valid
                                                                         SaveStoreMenuCategoryRequestDto requestDto) {

        storeMenuCategoryService.saveStoreMenuCategory(userId, requestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 메뉴 카테고리 이름 수정 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 카테고리의 이름 수정")
    @PatchMapping("/name")
    public ResponseDto<Void> updateStoreMenuCategoryName(@Parameter(hidden = true) @AuthInfo Long userId,
                                                                        @RequestBody
                                                                        @Valid
                                                                        UpdateStoreMenuCategoryNameRequestDto requestDto) {

        storeMenuCategoryService.updateStoreMenuCategoryName(userId, requestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 메뉴 카테고리 순서 수정 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 카테고리의 순서 수정")
    @PostMapping("/order")
    public ResponseDto<Void> updateStoreMenuCategoryOrder(@Parameter(hidden = true) @AuthInfo Long userId,
                                                                         @RequestBody
                                                                         @Valid
                                                                         UpdateStoreMenuCategoryOrderRequestDto requestDto) {

        storeMenuCategoryService.updateStoreMenuCategoryOrder(userId, requestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 메뉴 카테고리 삭제 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 카테고리 삭제")
    @DeleteMapping("/{storeId}/{storeMenuCategoryId}")
    public ResponseDto<Void> deleteStoreMenuCategory(@Parameter(hidden = true) @AuthInfo Long userId,
                                                     @PathVariable @NotNull Long storeId,
                                                     @PathVariable @NotNull Long storeMenuCategoryId) {

        storeMenuCategoryService.deleteStoreMenuCategory(userId, storeId, storeMenuCategoryId);
        return ResponseDto.onSuccess();
    }

}
