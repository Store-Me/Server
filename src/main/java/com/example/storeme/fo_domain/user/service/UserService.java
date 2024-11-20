package com.example.storeme.fo_domain.user.service;

import com.example.storeme.fo_domain.customer.domain.Customer;
import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.store.repository.StoreRepository;
import com.example.storeme.fo_domain.user.constant.RoleType;
import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.dto.user.*;
import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 관련 요청을 처리하는 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageFileService imageFileService;
    private final StoreRepository storeRepository;

    /**
     * 손님 정보를 반환하는 메서드
     */
    @Transactional(readOnly = true)
    public CustomerInfoResponseDto getCustomerInfo(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->{
            log.error("User not found with id: {}", userId);
            return new UserException(ErrorStatus._BAD_REQUEST);
        });
        Customer customer = user.getCustomer();

        return CustomerInfoResponseDto.builder()
                .accountId(user.getAccountId())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(customer.getProfileImageUrl())
                .hasAppId(user.getAccountId()!=null)
                .hasKakaoId(user.getKakaoId()!=null)
                .build();
    }

    /**
     * 사장님 정보를 반환하는 메서드
     */
    @Transactional(readOnly = true)
    public OwnerInfoResponseDto getOwnerInfo(Long userId, Long storeId){
        User user = userRepository.findById(userId).orElseThrow(()->{
            log.error("User not found with id: {}", userId);
            return new UserException(ErrorStatus._BAD_REQUEST);
        });
        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
                    log.error("Store not found with id: {}", storeId);
                    return new UserException(ErrorStatus._BAD_REQUEST);
                });

        return OwnerInfoResponseDto.builder()
                .accountId(user.getAccountId())
                .storeProfileImageUrl(store.getProfileImageUrl())
                .hasAppId(user.getAccountId()!=null)
                .hasKakaoId(user.getKakaoId()!=null)
                .build();
    }

    /**
     * 손님 정보를 변경하는 메서드
     */
    @Transactional
    public void updateCustomerInfo(Long userId, UpdateCustomerInfoRequestDto updateCustomerInfoRequestDto,
                                   MultipartFile profileImageFile){
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found with id: {}", userId);
            return new UserException(ErrorStatus._BAD_REQUEST);
        });

        if(user.getRoleType() == RoleType.OWNER){
            log.error("User role type is not Customer");
            throw new UserException(ErrorStatus._BAD_REQUEST);
        }

        Customer customer = user.getCustomer();

        // App 계정 Id 변경
        if(updateCustomerInfoRequestDto.getAccountId() != null &&
                !updateCustomerInfoRequestDto.getAccountId().equals(user.getAccountId()))
            user.setAccountId(updateCustomerInfoRequestDto.getAccountId());

        // 비밀번호 변경
        if(updateCustomerInfoRequestDto.getPassword()!=null &&
            !bCryptPasswordEncoder.matches(updateCustomerInfoRequestDto.getPassword(), user.getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(updateCustomerInfoRequestDto.getPassword()));

        // 닉네임 변경
        if(updateCustomerInfoRequestDto.getNickname()!=null &&
                !updateCustomerInfoRequestDto.getNickname().equals(customer.getNickname()))
            customer.setNickname(customer.getNickname());

        // 프로필 이미지 변경
        if(profileImageFile != null){
            imageFileService.deleteImageFile(customer.getProfileImageUrl());
            String profileImageFileUrl =
                    imageFileService.uploadImageFile(S3Folder.CUSTOMER_PROFILE_IMAGE, profileImageFile);
            customer.setProfileImageUrl(profileImageFileUrl);
        }

    }

    /**
     * 사장님 정보를 변경하는 메서드
     */
    @Transactional
    public void updateOwnerInfo(Long userId, UpdateOwnerInfoRequestDto updateOwnerInfoRequestDto,
                                MultipartFile storeProfileImageFile){
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found with id: {}", userId);
            return new UserException(ErrorStatus._BAD_REQUEST);
        });

        // App 계정 Id 변경
        if(updateOwnerInfoRequestDto.getAccountId() != null &&
                !updateOwnerInfoRequestDto.getAccountId().equals(user.getAccountId()))
            user.setAccountId(updateOwnerInfoRequestDto.getAccountId());

        // 비밀번호 변경
        if(updateOwnerInfoRequestDto.getPassword()!=null &&
                !bCryptPasswordEncoder.matches(updateOwnerInfoRequestDto.getPassword(), user.getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(updateOwnerInfoRequestDto.getPassword()));

        // 가게 프로필 이미지 변경
        if(updateOwnerInfoRequestDto.getStoreId()!=null &&
            storeProfileImageFile != null){

            Store store = storeRepository.findById(updateOwnerInfoRequestDto.getStoreId())
                            .orElseThrow(() -> {
                                log.error("Store not found with id: {}", updateOwnerInfoRequestDto.getStoreId());
                                return new UserException(ErrorStatus._BAD_REQUEST);
                            });

            imageFileService.deleteImageFile(store.getProfileImageUrl());
            String storeProfileImageFileUrl =
                    imageFileService.uploadImageFile(S3Folder.STORE_PROFILE_IMAGE, storeProfileImageFile);
            store.setProfileImageUrl(storeProfileImageFileUrl);
        }
    }

    /**
     * 손님 정보를 DB에 저장하는 메서드
     */
    @Transactional
    public void saveCustomerInfo(Long userId, SaveCustomerInfoRequestDto saveCustomerInfoRequestDto,
                                 MultipartFile profileImageFile){

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found with id: {}", userId);
            return new UserException(ErrorStatus._BAD_REQUEST);
        });

        Customer customer = user.getCustomer();

        // 닉네임 저장
        customer.setNickname(saveCustomerInfoRequestDto.getNickname());

        // 프로필 이미지 url 저장
        if(profileImageFile != null){
            String profileImageFileUrl =
                    imageFileService.uploadImageFile(S3Folder.CUSTOMER_PROFILE_IMAGE, profileImageFile);
            customer.setProfileImageUrl(profileImageFileUrl);
        }
    }

    /**
     * 회원 탈퇴를 수행하는 메서드
     */
    @Transactional
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
