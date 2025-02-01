package com.example.storeme.fo_domain.storeimage.controller;

import com.example.storeme.fo_domain.storeimage.dto.UpdateStoreImageOrderRequestDto;
import com.example.storeme.fo_domain.storeimage.service.StoreImageService;
import com.example.storeme.global.common.annotation.AuthInfo;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 가게 정보 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/store/image")
@Tag(name = "가게 이미지 관련 요청 처리")
public class StoreImageController {
    private final StoreImageService storeImageService;

    /**
     * 가게 이미지 저장 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 이미지 저장")
    @PostMapping(value = "/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Void> saveStoreImage(@Parameter(hidden = true) @AuthInfo Long userId,
            @NotNull @PathVariable Long storeId,
            @RequestPart(value = "storeImageFileList", required = false)
            List<MultipartFile> storeImageFileList) {

        storeImageService.saveStoreImageFile(userId, storeId, storeImageFileList);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 대표 이미지 설정을 처리하는 메서드
     */
    @Operation(summary = "가게 대표 이미지 설정")
    @PostMapping("/{storeId}/{storeImageId}")
    public ResponseDto<Void> setFeaturedStoreImage(@Parameter(hidden = true) @AuthInfo Long userId,
            @NotNull @PathVariable Long storeId,
            @NotNull @PathVariable Long storeImageId) {

        storeImageService.setFeaturedStoreImage(userId, storeId, storeImageId);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 이미지 삭제 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 이미지 삭제")
    @DeleteMapping("/{storeId}/{storeImageId}")
    public ResponseDto<Void> deleteStoreImage(@Parameter(hidden = true) @AuthInfo Long userId,
            @NotNull @PathVariable Long storeId,
            @NotNull @PathVariable Long storeImageId) {

        storeImageService.deleteStoreImageFile(userId, storeId, storeImageId);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 이미지 순서 값 수정 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 이미지 순서 수정")
    @PatchMapping("/order")
    public ResponseDto<Void> updateStoreImageOrder(@Parameter(hidden = true) @AuthInfo Long userId,
            @Valid @RequestBody
            UpdateStoreImageOrderRequestDto updateStoreImageOrderRequestDto) {
        storeImageService.updateStoreImageOrder(userId, updateStoreImageOrderRequestDto);
        return ResponseDto.onSuccess();
    }
}
