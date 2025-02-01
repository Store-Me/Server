package com.example.storeme.fo_domain.storeimage.service;

import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.store.repository.StoreRepository;
import com.example.storeme.fo_domain.storeimage.domain.StoreImage;
import com.example.storeme.fo_domain.storeimage.dto.UpdateStoreImageOrderRequestDto;
import com.example.storeme.fo_domain.storeimage.exception.StoreImageException;
import com.example.storeme.fo_domain.storeimage.repository.StoreImageRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final ImageFileService imageFileService;

    /**
     * 가게 사진을 저장하는 메서드
     */
    @Transactional
    public void saveStoreImageFile(Long userId, Long storeId, List<MultipartFile> storeImageFileList) {
        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            log.error("The store is not for the user");
            throw new StoreImageException(ErrorStatus._BAD_REQUEST);
        }

        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
            log.error("Store not found with id: {}", storeId);
            return new StoreImageException(ErrorStatus._BAD_REQUEST);
        });

        if (!ObjectUtils.isEmpty(storeImageFileList) && !storeImageFileList.isEmpty()) {
            List<String> uploadedUrlList = imageFileService.uploadImageFileList(S3Folder.STORE_IMAGE, storeImageFileList);
            List<StoreImage> storeImageList = IntStream.range(0, storeImageFileList.size())
                    .mapToObj(index -> StoreImage.builder()
                            .imageUrl(uploadedUrlList.get(index))
                            .order(store.getStoreImageList().size() + index)
                            .build())
                    .toList();

            store.addStoreImageList(storeImageList);
        }
    }

    @Transactional
    public void setFeaturedStoreImage(Long userId, Long storeId, Long storeImageId){
        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            log.error("The store is not for the user");
            throw new StoreImageException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeImageRepository.existsByIdAndStore_Id(storeImageId, storeId)) {
            log.error("The storeImage is not for the store");
            throw new StoreImageException(ErrorStatus._BAD_REQUEST);
        }

        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
            log.error("Store not found with id: {}", storeId);
            return new StoreImageException(ErrorStatus._BAD_REQUEST);
        });

        StoreImage storeImage = storeImageRepository.findById(storeImageId).orElseThrow(() -> {
            log.error("Store image not found with id: {}", storeImageId);
            return new StoreImageException(ErrorStatus._BAD_REQUEST);
        });

        store.setFeaturedImageUrl(storeImage.getImageUrl());
    }

    /**
     * 가게 사진을 삭제하는 메서드
     */
    @Transactional
    public void deleteStoreImageFile(Long userId, Long storeId, Long storeImageId) {
        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            log.error("The store is not for the user");
            throw new StoreImageException(ErrorStatus._BAD_REQUEST);
        }

        if (!storeImageRepository.existsByIdAndStore_Id(storeImageId, storeId)) {
            log.error("The storeImage is not for the store");
            throw new StoreImageException(ErrorStatus._BAD_REQUEST);
        }

        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
            log.error("Store not found with id: {}", storeId);
            return new StoreImageException(ErrorStatus._BAD_REQUEST);
        });

        StoreImage storeImage = storeImageRepository.findById(storeImageId).orElseThrow(() -> {
            log.error("Store image not found with id: {}", storeImageId);
            return new StoreImageException(ErrorStatus._BAD_REQUEST);
        });

        if(store.getFeaturedImageUrl().equals(storeImage.getImageUrl())) {
            store.setFeaturedImageUrl(null);
        }

        storeImageRepository.deleteById(storeImageId);
    }

    /**
     * 가게 사진 순서값을 수정하는 메서드
     */
    @Transactional
    public void updateStoreImageOrder(Long userId,
            UpdateStoreImageOrderRequestDto updateStoreImageOrderRequestDto) {

        Long storeId = updateStoreImageOrderRequestDto.getStoreId();

        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            log.error("The store is not for the user");
            throw new StoreImageException(ErrorStatus._BAD_REQUEST);
        }

        updateStoreImageOrderRequestDto.getStoreImageOrderInfoList()
                .forEach(storeImageOrderInfoDto -> {
                    Long storeImageId = storeImageOrderInfoDto.getStoreImageId();

                    if (!storeImageRepository.existsByIdAndStore_Id(storeImageId, storeId)) {
                        log.error("The storeImage is not for the store");
                        throw new StoreImageException(ErrorStatus._BAD_REQUEST);
                    }

                    StoreImage storeImage = storeImageRepository.findById(storeImageId).orElseThrow(() -> {
                        log.error("Store image not found with id: {}", storeImageId);
                        return new StoreImageException(ErrorStatus._BAD_REQUEST);
                    });

                    storeImage.setOrder(storeImage.getOrder());
                });
    }
}
