package com.example.storeme.fo_domain.storemenu.service;

import com.example.storeme.fo_domain.store.repository.StoreRepository;
import com.example.storeme.fo_domain.storemenu.domain.StoreMenu;
import com.example.storeme.fo_domain.storemenu.domain.StoreMenuCategory;
import com.example.storeme.fo_domain.storemenu.dto.SaveStoreMenuRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.StoreMenuListResponseDto;
import com.example.storeme.fo_domain.storemenu.dto.StoreMenuListResponseDto.StoreMenuInfoDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuCategoryOrderRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuOrderRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuRequestDto;
import com.example.storeme.fo_domain.storemenu.exception.StoreMenuCategoryException;
import com.example.storeme.fo_domain.storemenu.exception.StoreMenuException;
import com.example.storeme.fo_domain.storemenu.repository.StoreMenuCategoryRepository;
import com.example.storeme.fo_domain.storemenu.repository.StoreMenuRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.service.ImageFileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreMenuService {

    private final StoreRepository storeRepository;
    private final StoreMenuCategoryRepository storeMenuCategoryRepository;
    private final StoreMenuRepository storeMenuRepository;
    private final ImageFileService imageFileService;

    @Transactional(readOnly = true)
    public StoreMenuListResponseDto getStoreMenuList(Long userId, Long storeId, Long storeMenuCategoryId) {
        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            log.error("The store is not for the user");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuCategoryRepository.existsByIdAndStore_Id(
                storeMenuCategoryId, storeId)) {
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        List<StoreMenuInfoDto> storeMenuCategoryInfoList =
                storeMenuRepository.findByStoreMenuCategory_Id(storeMenuCategoryId)
                        .stream()
                        .map(StoreMenuListResponseDto::toDto)
                        .toList();

        return StoreMenuListResponseDto.builder()
                .storeMenuInfoList(storeMenuCategoryInfoList)
                .build();
    }

    @Transactional
    public void saveStoreMenu(Long userId, SaveStoreMenuRequestDto requestDto, MultipartFile storeMenuImageFile) {
        if (!storeRepository.existsByIdAndUser_Id(requestDto.storeId(), userId)) {
            log.error("The store is not for the user");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuCategoryRepository.existsByIdAndStore_Id(
                requestDto.storeMenuCategoryId(), requestDto.storeId())) {
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        StoreMenuCategory storeMenuCategory = storeMenuCategoryRepository.findById(requestDto.storeMenuCategoryId())
                .orElseThrow(() -> {
                    log.error("StoreMenuCategory not found with id: {}", requestDto.storeMenuCategoryId());
                    return new StoreMenuException(ErrorStatus._BAD_REQUEST);
                });

        String menuImageFileUrl = imageFileService.uploadImageFile(S3Folder.MENU_IMAGE, storeMenuImageFile);

        storeMenuCategory.addStoreMenu(StoreMenu.builder()
                .name(requestDto.name())
                .order(requestDto.order())
                .priceType(requestDto.priceType())
                .fixedPrice(requestDto.fixedPrice())
                .rangeMaxPrice(requestDto.rangeMaxPrice())
                .rangeMinPrice(requestDto.rangeMinPrice())
                .description(requestDto.description())
                .imageUrl(menuImageFileUrl)
                .isSignature(requestDto.isSignature())
                .isPopular(requestDto.isPopular())
                .isRecommended(requestDto.isRecommended())
                .build());
    }

    @Transactional
    public void updateStoreMenu(Long userId, UpdateStoreMenuRequestDto requestDto, MultipartFile storeMenuImageFile) {
        if (!storeRepository.existsByIdAndUser_Id(requestDto.storeId(), userId)) {
            log.error("The store is not for the user");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuCategoryRepository.existsByIdAndStore_Id(
                requestDto.storeMenuCategoryId(), requestDto.storeId())) {
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuRepository.existsByIdAndStoreMenuCategory_Id(requestDto.storeMenuId(), requestDto.storeMenuCategoryId())) {
            log.error("The storeMenu is not for the storeMenuCategory");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        StoreMenu storeMenu = storeMenuRepository.findById(requestDto.storeMenuId())
                .orElseThrow(() -> {
                    log.error("StoreMenu not found for id: {}", requestDto.storeMenuId());
                    return new StoreMenuException(ErrorStatus._BAD_REQUEST);
                });

        if(storeMenuImageFile != null && storeMenuImageFile.isEmpty())
            storeMenu.setImageUrl(null);
        else if(storeMenuImageFile != null){
            String menuImageFileUrl = imageFileService.uploadImageFile(
                    S3Folder.STORE_PROFILE_IMAGE, storeMenuImageFile);
            imageFileService.deleteImageFile(storeMenu.getImageUrl());

            storeMenu.setImageUrl(menuImageFileUrl);
        }

        if(requestDto.name().isPresent())
            storeMenu.setName(requestDto.name().get());
        if(requestDto.order().isPresent())
            storeMenu.setOrder(requestDto.order().get());
        if(requestDto.priceType().isPresent())
            storeMenu.setPriceType(requestDto.priceType().get());
        if(requestDto.fixedPrice().isPresent())
            storeMenu.setFixedPrice(requestDto.fixedPrice().get());
        if(requestDto.rangeMaxPrice().isPresent())
            storeMenu.setRangeMaxPrice(requestDto.rangeMaxPrice().get());
        if(requestDto.rangeMinPrice().isPresent())
            storeMenu.setRangeMinPrice(requestDto.rangeMinPrice().get());
        if(requestDto.description().isPresent())
            storeMenu.setDescription(requestDto.description().get());
        if(requestDto.isSignature().isPresent())
            storeMenu.setIsSignature(requestDto.isSignature().get());
        if(requestDto.isPopular().isPresent())
            storeMenu.setIsPopular(requestDto.isPopular().get());
        if(requestDto.isRecommended().isPresent())
            storeMenu.setIsRecommended(requestDto.isRecommended().get());
        if(requestDto.isRecommended().isPresent())
            storeMenu.setIsRecommended(requestDto.isRecommended().get());

    }

    @Transactional
    public void updateStoreMenuCategoryOrder(Long userId, UpdateStoreMenuOrderRequestDto requestDto) {
        if (!storeRepository.existsByIdAndUser_Id(requestDto.storeId(), userId)) {
            log.error("The store is not for the user");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuCategoryRepository.existsByIdAndStore_Id(
                requestDto.storeMenuCategoryId(), requestDto.storeId())) {
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        requestDto.storeMenuOrderInfoList()
                .forEach(storeMenuCategoryInfo -> {
                    Long storeMenuId = storeMenuCategoryInfo.storeMenuId();

                    if (!storeMenuRepository.existsByIdAndStoreMenuCategory_Id(
                            storeMenuId, requestDto.storeMenuCategoryId())) {
                        log.error("The storeMenu is not for the storeMenuCategory");
                        throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
                    }

                    StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
                            .orElseThrow(() -> {
                                log.error("StoreMenu not found for id: {}", storeMenuId);
                                return new StoreMenuException(ErrorStatus._BAD_REQUEST);
                            });

                    storeMenu.setOrder(storeMenuCategoryInfo.order());
                });
    }

    @Transactional
    public void deleteStoreMenu(Long userId, Long storeId, Long storeMenuCategoryId, Long storeMenuId) {
        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            log.error("The store is not for the user");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuCategoryRepository.existsByIdAndStore_Id(
                storeMenuCategoryId, storeId)) {
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeMenuRepository.existsByIdAndStoreMenuCategory_Id(storeMenuId, storeMenuCategoryId)) {
            log.error("The storeMenu is not for the storeMenuCategory");
            throw new StoreMenuException(ErrorStatus._BAD_REQUEST);
        }

        StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
                .orElseThrow(() -> {
                    log.error("StoreMenu not found for id: {}", storeMenuId);
                    return new StoreMenuException(ErrorStatus._BAD_REQUEST);
                });

        storeMenuRepository.decrementOrdersGreaterThan(storeMenu.getOrder(), storeMenuCategoryId);

        storeMenuRepository.deleteById(storeMenuId);
    }
}
