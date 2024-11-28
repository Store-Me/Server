package com.example.storeme.fo_domain.store.controller;

import com.example.storeme.fo_domain.store.dto.SaveStoreInfoRequestDto;
import com.example.storeme.fo_domain.store.dto.StoreInfoListResponseDto;
import com.example.storeme.fo_domain.store.dto.StoreInfoResponseDto;
import com.example.storeme.fo_domain.store.dto.UpdateStoreInfoRequestDto;
import com.example.storeme.fo_domain.store.service.StoreService;
import com.example.storeme.global.common.annotation.AuthInfo;
import com.example.storeme.global.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
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
@RequestMapping("/store")
@Tag(name = "가게 관련 요청 처리")
public class StoreController {

    private final StoreService storeService;

    /**
     * 유저가 관리하는 가게 정보 리스트 조회 요청을 처리하는 메서드
     */
    @Operation(summary = "유저의 가게 정보 리스트 조회")
    @GetMapping("/list")
    public ResponseDto<StoreInfoListResponseDto> getStoreInfoList(@Parameter(hidden = true) @AuthInfo Long userId) {

        StoreInfoListResponseDto storeInfoListResponseDto = storeService.getStoreInfoList(userId);
        return ResponseDto.onSuccess(storeInfoListResponseDto);
    }

    /**
     * 가게 정보 조회 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 정보 조회")
    @GetMapping("/{storeId}")
    public ResponseDto<StoreInfoResponseDto> getStoreInfo(@Parameter(hidden = true) @AuthInfo Long userId,
                                                          @NotNull @PathVariable Long storeId) {

        StoreInfoResponseDto storeInfoResponseDto = storeService.getStoreInfo(userId, storeId);
        return ResponseDto.onSuccess(storeInfoResponseDto);
    }

    /**
     * 가게 정보 저장 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 정보 저장")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Void> saveStoreInfo(@Parameter(hidden = true) @AuthInfo Long userId,
            @RequestPart("saveStoreInfoRequestDto")
            @Parameter(
                    content = @Content(
                            encoding = @Encoding(name = "saveStoreInfoRequestDto",
                                    contentType = MediaType.APPLICATION_JSON_VALUE)))
            @Valid SaveStoreInfoRequestDto saveStoreInfoRequestDto,
            @RequestPart(value = "storeProfileImageFile", required = false) MultipartFile storeProfileImageFile,
            @RequestPart(value = "storeFeaturedImageFile", required = false) MultipartFile storeFeaturedImageFile,
            @RequestPart(value = "storeImageFileList", required = false) List<MultipartFile> storeImageFileList,
            @RequestPart(value = "storeBannerImage", required = false) MultipartFile storeBannerImageFile) {

        storeService.saveStoreInfo(userId, saveStoreInfoRequestDto, storeProfileImageFile,
                storeFeaturedImageFile, storeImageFileList, storeBannerImageFile);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 정보 수정 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 정보 수정")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<Void> updateStoreInfo(@Parameter(hidden = true) @AuthInfo Long userId,
            @RequestPart("updateStoreInfoRequestDto")
            @Parameter(
                    content = @Content(
                            encoding = @Encoding(name = "updateStoreInfoRequestDto",
                                    contentType = MediaType.APPLICATION_JSON_VALUE)))
            @Valid UpdateStoreInfoRequestDto updateStoreInfoRequestDto,
            @RequestPart(value = "storeProfileImageFile", required = false) MultipartFile storeProfileImageFile,
            @RequestPart(value = "storeBannerImageFile", required = false) MultipartFile storeBannerImageFile) {

        storeService.updateStoreInfo(userId, updateStoreInfoRequestDto, storeProfileImageFile, storeBannerImageFile);
        return ResponseDto.onSuccess();
    }

    /**
     * 가게 정보 삭제 요청을 처리하는 메서드
     */
    @Operation(summary = "가게 정보 삭제")
    @DeleteMapping("/{storeId}")
    public ResponseDto<Void> deleteStoreInfo(@Parameter(hidden = true) @AuthInfo Long userId,
                                             @NotNull @PathVariable Long storeId) {

        storeService.deleteStoreInfo(userId, storeId);
        return ResponseDto.onSuccess();
    }
}
