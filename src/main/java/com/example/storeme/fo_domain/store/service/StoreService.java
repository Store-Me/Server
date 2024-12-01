package com.example.storeme.fo_domain.store.service;

import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.store.dto.SaveStoreInfoRequestDto;
import com.example.storeme.fo_domain.store.dto.StoreInfoListResponseDto;
import com.example.storeme.fo_domain.store.dto.StoreInfoResponseDto;
import com.example.storeme.fo_domain.store.dto.UpdateStoreInfoRequestDto;
import com.example.storeme.fo_domain.store.exception.StoreException;
import com.example.storeme.fo_domain.store.repository.StoreRepository;
import com.example.storeme.fo_domain.storeimage.domain.StoreImage;
import com.example.storeme.fo_domain.storeimage.repository.StoreImageRepository;
import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final ImageFileService imageFileService;

    /**
     * 유저가 관리하는 가게 정보 리스트를 반환하는 메서드
     */
    @Transactional(readOnly = true)
    public StoreInfoListResponseDto getStoreInfoList(Long userId){

        return StoreInfoListResponseDto.builder()
                .storeInfoList(storeRepository.findStoreInfoByUserId(userId))
                .build();
    }

    /**
     * 가게 정보를 반환하는 메서드
     */
    @Transactional(readOnly = true)
    public StoreInfoResponseDto getStoreInfo(Long userId, Long storeId){

        if(!storeRepository.existsByIdAndUser_Id(storeId, userId)){
            log.error("The store is not for the user");
            throw new StoreException(ErrorStatus._BAD_REQUEST);
        }

        Store store = storeRepository.findById(storeId).orElseThrow(()->{
            log.error("Store not found with id: {}", storeId);
            return new StoreException(ErrorStatus._BAD_REQUEST);
        });

        return StoreInfoResponseDto.builder()
                .storeName(store.getName())
                .storeProfileImageUrl(store.getProfileImageUrl())
                .storeFeaturedImageUrl(store.getFeaturedImageUrl())
                .storeImageInfoList(store.getStoreImageList().stream()
                        .map((storeImage)-> StoreInfoResponseDto.StoreImageInfoDto.builder()
                                .storeImageId(storeImage.getId())
                                .storeImageUrl(storeImage.getImageUrl())
                                .order(storeImage.getOrder())
                                .build())
                        .sorted(Comparator.comparingInt(StoreInfoResponseDto.StoreImageInfoDto::getOrder))
                        .toList())
                .storeBannerImageUrl(store.getBannerImageUrl())
                .storeDescription(store.getDescription())
                .storeCategory(store.getCategory())
                .storeDetailCategory(store.getDetailCategory())
                .storeLocation(store.getLocation())
                .storeLocationCode(store.getLocationCode())
                .storeLocationDetail(store.getLocationDetail())
                .storeLat(store.getLat())
                .storeLng(store.getLng())
                .storePhoneNumber(store.getPhoneNumber())
                .storeNotice(store.getNotice())
                .storeIntro(store.getIntro())
                .build();
    }

    /**
     * 가게 정보를 저장하는 메서드
     */
    @Transactional
    public void saveStoreInfo(Long userId, SaveStoreInfoRequestDto saveStoreInfoRequestDto,
                              MultipartFile storeProfileImageFile,
                              MultipartFile storeFeaturedImageFile,
                              List<MultipartFile> storeImageFileList,
                              MultipartFile storeBannerImageFile){

        User user = userRepository.findById(userId).orElseThrow(()->{
            log.error("User not found with id: {}", userId);
            return new UserException(ErrorStatus._BAD_REQUEST);
        });

        String storeProfileImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_PROFILE_IMAGE, storeProfileImageFile);
        String storeFeaturedImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_IMAGE, storeFeaturedImageFile);
        List<StoreImage> storeImageFileUrlList = IntStream.range(0, storeImageFileList.size())
                .mapToObj(index -> StoreImage.builder()
                        .imageUrl(imageFileService.uploadImageFileList(S3Folder.STORE_IMAGE, storeImageFileList).get(index))
                        .order(index)
                        .build())
                .toList();
        String storeBannerImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_BANNER_IMAGE, storeBannerImageFile);

        Store store = Store.builder()
                .name(saveStoreInfoRequestDto.getStoreName())
                .profileImageUrl(storeProfileImageFileUrl)
                .featuredImageUrl(storeFeaturedImageFileUrl)
                .bannerImageUrl(storeBannerImageFileUrl)
                .description(saveStoreInfoRequestDto.getStoreDescription())
                .category(saveStoreInfoRequestDto.getStoreCategory())
                .detailCategory(saveStoreInfoRequestDto.getStoreDetailCategory())
                .location(saveStoreInfoRequestDto.getStoreLocation())
                .locationCode(saveStoreInfoRequestDto.getStoreLocationCode())
                .locationDetail(saveStoreInfoRequestDto.getStoreLocationDetail())
                .lat(saveStoreInfoRequestDto.getStoreLat())
                .lng(saveStoreInfoRequestDto.getStoreLng())
                .phoneNumber(saveStoreInfoRequestDto.getStorePhoneNumber())
                .intro(saveStoreInfoRequestDto.getStoreIntro())
                .notice(saveStoreInfoRequestDto.getStoreNotice())
                .build();

        store.addStoreImageList(storeImageFileUrlList);
        user.addStore(store);
    }

    /**
     * 가게 정보를 수정하는 메서드
     */
    @Transactional
    public void updateStoreInfo(Long userId, UpdateStoreInfoRequestDto updateStoreInfoRequestDto,
                                MultipartFile storeProfileImageFile,
                                MultipartFile storeBannerImageFile){

        if(!storeRepository.existsByIdAndUser_Id(updateStoreInfoRequestDto.getStoreId(), userId)){
            log.error("The store is not for the user");
            throw new StoreException(ErrorStatus._BAD_REQUEST);
        }

        Store store = storeRepository.findById(updateStoreInfoRequestDto.getStoreId()).orElseThrow(()->{
            log.error("Store not found with id: {}", updateStoreInfoRequestDto.getStoreId());
            return new StoreException(ErrorStatus._BAD_REQUEST);
        });

        if(storeProfileImageFile != null && storeProfileImageFile.isEmpty())
            store.setProfileImageUrl(null);
        else if(storeProfileImageFile != null){
            String storeProfileImageFileUrl = imageFileService.uploadImageFile(
                    S3Folder.STORE_PROFILE_IMAGE, storeProfileImageFile);
            imageFileService.deleteImageFile(store.getProfileImageUrl());

            store.setProfileImageUrl(storeProfileImageFileUrl);
        }

        if(storeBannerImageFile != null && storeBannerImageFile.isEmpty())
            store.setBannerImageUrl(null);
        else if(storeBannerImageFile != null){
            String storeBannerImageFileUrl = imageFileService.uploadImageFile(
                    S3Folder.STORE_BANNER_IMAGE, storeBannerImageFile
            );
            imageFileService.deleteImageFile(store.getBannerImageUrl());

            store.setBannerImageUrl(storeBannerImageFileUrl);
        }


        if(updateStoreInfoRequestDto.getStoreName().isPresent())
            store.setName(updateStoreInfoRequestDto.getStoreName().get());

        if(updateStoreInfoRequestDto.getStoreDescription().isPresent())
            store.setDescription(updateStoreInfoRequestDto.getStoreDescription().get());

        if(updateStoreInfoRequestDto.getStoreCategory().isPresent())
            store.setCategory(updateStoreInfoRequestDto.getStoreCategory().get());

        if(updateStoreInfoRequestDto.getStoreDetailCategory().isPresent())
            store.setDetailCategory(updateStoreInfoRequestDto.getStoreDetailCategory().get());

        if(updateStoreInfoRequestDto.getStoreFeaturedImageId().isPresent()){
            if(updateStoreInfoRequestDto.getStoreFeaturedImageId().get() == null)
                store.setFeaturedImageUrl(null);
            else
                store.setFeaturedImageUrl(
                        storeImageRepository.findById(updateStoreInfoRequestDto.getStoreFeaturedImageId().get())
                                .orElseThrow(()->{
                                    log.error("Store image not found for id: {}", updateStoreInfoRequestDto.getStoreFeaturedImageId().get());
                                    return new StoreException(ErrorStatus._BAD_REQUEST);
                                }).getImageUrl());
        }

        if(updateStoreInfoRequestDto.getStoreLocation().isPresent())
            store.setLocation(updateStoreInfoRequestDto.getStoreLocation().get());

        if(updateStoreInfoRequestDto.getStoreLocationCode().isPresent())
            store.setLocationCode(updateStoreInfoRequestDto.getStoreLocationCode().get());

        if(updateStoreInfoRequestDto.getStoreLocationDetail().isPresent())
            store.setLocationDetail(updateStoreInfoRequestDto.getStoreLocationDetail().get());

        if(updateStoreInfoRequestDto.getStoreLat().isPresent())
            store.setLat(updateStoreInfoRequestDto.getStoreLat().get());

        if(updateStoreInfoRequestDto.getStoreLng().isPresent())
            store.setLng(updateStoreInfoRequestDto.getStoreLng().get());

        if(updateStoreInfoRequestDto.getStorePhoneNumber().isPresent())
            store.setPhoneNumber(updateStoreInfoRequestDto.getStorePhoneNumber().get());

        if(updateStoreInfoRequestDto.getStoreIntro().isPresent())
            store.setIntro(updateStoreInfoRequestDto.getStoreIntro().get());

        if(updateStoreInfoRequestDto.getStoreNotice().isPresent())
            store.setNotice(updateStoreInfoRequestDto.getStoreNotice().get());

    }

    /**
     * 가게 정보를 삭제하는 메서드
     */
    @Transactional
    public void deleteStoreInfo(Long userId, Long storeId){

        if(!storeRepository.existsById(storeId)){
            log.error("Store not found with id: {}", storeId);
            throw new StoreException(ErrorStatus._BAD_REQUEST);
        }

        if(!storeRepository.existsByIdAndUser_Id(storeId, userId)){
            log.error("The store is not for the user");
            throw new StoreException(ErrorStatus._BAD_REQUEST);
        }

        storeRepository.deleteById(storeId);
    }
}
