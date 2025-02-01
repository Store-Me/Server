package com.example.storeme.fo_domain.storemenu.service;

import com.example.storeme.fo_domain.storemenu.domain.StoreMenuCategory;
import com.example.storeme.fo_domain.storemenu.dto.SaveStoreMenuCategoryRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.StoreMenuCategoryListResponseDto;
import com.example.storeme.fo_domain.storemenu.dto.StoreMenuCategoryListResponseDto.StoreMenuCategoryInfoDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuCategoryNameRequestDto;
import com.example.storeme.fo_domain.storemenu.dto.UpdateStoreMenuCategoryOrderRequestDto;
import com.example.storeme.fo_domain.storemenu.exception.StoreMenuCategoryException;
import com.example.storeme.fo_domain.storemenu.repository.StoreMenuCategoryRepository;
import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.store.repository.StoreRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreMenuCategoryService {

    private final StoreMenuCategoryRepository storeMenuCategoryRepository;
    private final StoreRepository storeRepository;

    /**
     * 가게 메뉴 카테고리 리스트를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public StoreMenuCategoryListResponseDto getStoreMenuCategoryList(Long userId, Long storeId){
        if(!storeRepository.existsByIdAndUser_Id(storeId, userId)){
            log.error("The store is not for the user");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        List<StoreMenuCategoryInfoDto> storeMenuCategoryInfoList =
                storeMenuCategoryRepository.findStoreMenuCategoryInfoByUserId(storeId);

        return StoreMenuCategoryListResponseDto.builder()
                .storeMenuCategoryInfoList(storeMenuCategoryInfoList)
                .build();
    }

    /**
     * 가게 메뉴 카테고리를 저장하는 메서드
     */
    @Transactional
    public void saveStoreMenuCategory(Long userId, SaveStoreMenuCategoryRequestDto requestDto){
        if(!storeRepository.existsByIdAndUser_Id(requestDto.getStoreId(), userId)){
            log.error("The store is not for the user");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        Store store = storeRepository.findById(requestDto.getStoreId()).orElseThrow(()->{
            log.error("Store not found with id: {}", requestDto.getStoreId());
            return new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        });

        store.addStoreMenuCategory(StoreMenuCategory.builder()
                .category(requestDto.getStoreMenuCategory())
                .order(store.getStoreMenuCategoryList().size())
                .build());
    }

    /**
     * 가게 메뉴 카테고리 이름을 변경하는 메서드
     */
    @Transactional
    public void updateStoreMenuCategoryName(Long userId, UpdateStoreMenuCategoryNameRequestDto requestDto){
        if(!storeRepository.existsByIdAndUser_Id(requestDto.getStoreId(), userId)){
            log.error("The store is not for the user");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        if(!storeMenuCategoryRepository.existsByIdAndStore_Id(
                requestDto.getStoreMenuCategoryId(), requestDto.getStoreId())){
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        StoreMenuCategory storeMenuCategory = storeMenuCategoryRepository.findById(requestDto.getStoreMenuCategoryId())
                .orElseThrow(()->{
                   log.error("StoreMenuCategory not found for id: {}", requestDto.getStoreMenuCategoryId());
                   return new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
                });

        storeMenuCategory.setCategory(requestDto.getStoreMenuCategory());
    }

    /**
     * 가게 메뉴 카테고리 순서를 변경하는 메서드
     */
    @Transactional
    public void updateStoreMenuCategoryOrder(Long userId, UpdateStoreMenuCategoryOrderRequestDto requestDto){
        if(!storeRepository.existsByIdAndUser_Id(requestDto.getStoreId(), userId)){
            log.error("The store is not for the user");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        requestDto.getStoreMenuCategoryOrderInfoList()
                .forEach(storeMenuCategoryOrderInfoDto -> {
                    Long storeMenuCategoryId = storeMenuCategoryOrderInfoDto.getStoreMenuCategoryId();

                    if(!storeMenuCategoryRepository.existsByIdAndStore_Id(
                            storeMenuCategoryId, requestDto.getStoreId())){
                        log.error("The StoreMenuCategory is not for the store");
                        throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
                    }

                    StoreMenuCategory storeMenuCategory = storeMenuCategoryRepository
                            .findById(storeMenuCategoryId)
                            .orElseThrow(()->{
                                log.error("StoreMenuCategory not found for id: {}", storeMenuCategoryId);
                                return new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
                            });

                    storeMenuCategory.setOrder(storeMenuCategoryOrderInfoDto.getStoreMenuCategoryOrder());
                });
    }

    /**
     * 가게 메뉴 카테고리를 삭제하는 메서드
     *
     * 해당 카테고리에 속한 모든 메뉴들은 삭제된다.
     */
    @Transactional
    public void deleteStoreMenuCategory(Long userId, Long storeId, Long storeMenuCategoryId){
        if(!storeRepository.existsByIdAndUser_Id(storeId, userId)){
            log.error("The store is not for the user");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        if(!storeMenuCategoryRepository.existsByIdAndStore_Id(
                storeMenuCategoryId, storeId)){
            log.error("The StoreMenuCategory is not for the store");
            throw new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
        }

        StoreMenuCategory storeMenuCategory = storeMenuCategoryRepository.findById(storeMenuCategoryId)
                        .orElseThrow(() -> {
                            log.error("StoreMenuCategory not found for id: {}", storeMenuCategoryId);
                            return new StoreMenuCategoryException(ErrorStatus._BAD_REQUEST);
                        });

        // 삭제되는 카테고리의 order 값보다 큰 엔티티의 order값을 1씩 감소
        storeMenuCategoryRepository.decrementOrdersGreaterThan(storeMenuCategory.getOrder(), storeId);

        storeMenuCategoryRepository.delete(storeMenuCategory);
    }
}
