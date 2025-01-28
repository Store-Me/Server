package com.example.storeme.fo_domain.storemenu.controller;

import com.example.storeme.fo_domain.storemenu.dto.SaveStoreMenuRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.StoreMenuListResponseDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuOrderRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuRequestDto;
import com.example.storeme.fo_domain.storemenu.service.StoreMenuService;
import com.example.storeme.global.common.annotation.AuthInfo;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 가게 메뉴 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/store/menu")
@Tag(name = "가게 메뉴 관련 요청 처리")
public class StoreMenuController {

    private final StoreMenuService storeMenuService;

    /**
     * 가게 메뉴 정보 조회를 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 전체 조회")
    @GetMapping("/{storeId}/{storeMenuCategoryId}")
    public ResponseDto<StoreMenuListResponseDto> getStoreMenuList(@Parameter(hidden = true) @AuthInfo Long userId,
            @PathVariable @Positive Long storeId,
            @PathVariable @Positive Long storeMenuCategoryId) {
        StoreMenuListResponseDto storeMenuListResponseDto =
                storeMenuService.getStoreMenuList(userId, storeId, storeMenuCategoryId);

        return ResponseDto.onSuccess(storeMenuListResponseDto);
    }

    /**
     * 가게 메뉴 저장 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 저장")
    @PostMapping
    public ResponseDto<Void> saveStoreMenu(@Parameter(hidden = true) @AuthInfo Long userId,
            @RequestPart("saveStoreMenuRequestDto")
            @Parameter(
                    content = @Content(
                            encoding = @Encoding(name = "saveStoreMenuRequestDto",
                                    contentType = MediaType.APPLICATION_JSON_VALUE)))
            @Valid SaveStoreMenuRequestDto requestDto,
            @RequestPart(value = "storeMenuImageFile", required = false) MultipartFile storeMenuImageFile) {

        storeMenuService.saveStoreMenu(userId, requestDto, storeMenuImageFile);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 메뉴수정 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 수정")
    @PatchMapping
    public ResponseDto<Void> updateStoreMenu(@Parameter(hidden = true) @AuthInfo Long userId,
            @RequestPart("updateStoreMenuRequestDto")
            @Parameter(
                    content = @Content(
                            encoding = @Encoding(name = "updateStoreMenuRequestDto",
                                    contentType = MediaType.APPLICATION_JSON_VALUE)))
            @Valid UpdateStoreMenuRequestDto requestDto,
            @RequestPart(value = "storeMenuImageFile", required = false) MultipartFile storeMenuImageFile) {

        storeMenuService.updateStoreMenu(userId, requestDto, storeMenuImageFile);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 메뉴 순서 수정 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴의 순서 수정")
    @PatchMapping("/order")
    public ResponseDto<Void> updateStoreMenuOrder(@Parameter(hidden = true) @AuthInfo Long userId,
            @RequestBody
            @Valid
            UpdateStoreMenuOrderRequestDto requestDto) {

        storeMenuService.updateStoreMenuCategoryOrder(userId, requestDto);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 메뉴 삭제 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 메뉴 삭제")
    @DeleteMapping("/{storeId}/{storeMenuCategoryId}/{storeMenuId}")
    public ResponseDto<Void> deleteStoreMenu(@Parameter(hidden = true) @AuthInfo Long userId,
            @PathVariable @NotNull @Positive Long storeId,
            @PathVariable @NotNull @Positive Long storeMenuCategoryId,
            @PathVariable @NotNull @Positive Long storeMenuId) {

        storeMenuService.deleteStoreMenu(userId, storeId, storeMenuCategoryId, storeMenuId);
        return ResponseDto.onSuccess();
    }
}
