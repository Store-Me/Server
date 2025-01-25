package com.example.storeme.fo_domain.user.service;

import com.example.storeme.fo_domain.customer.domain.Customer;
import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.storeimage.domain.StoreImage;
import com.example.storeme.fo_domain.user.constant.RoleType;
import com.example.storeme.fo_domain.user.constant.VerificationProperty;
import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.dto.signup.*;
import com.example.storeme.fo_domain.user.dto.verification.ConfirmCodeRequestDto;
import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.constant.RedisKeyPrefix;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.dto.FileUploadRequest;
import com.example.storeme.global.config.s3.exception.ImageFileException;
import com.example.storeme.global.config.s3.service.ImageFileService;
import com.example.storeme.global.util.StringRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.example.storeme.fo_domain.user.constant.SignupType.KAKAO;
import static com.example.storeme.fo_domain.user.dto.signup.SignupModeResponseDto.SignupMode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * 회원가입 관련 기능을 처리하는 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {
    private final VerificationService verificationService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StringRedisUtil stringRedisUtil;
    private final ImageFileService imageFileService;

    /**
     * App 계정 손님타입 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleAppCustomerSignup(AppCustomerSignupRequestDto appCustomerSignupRequestDto,
                                MultipartFile profileImageFile) {

        confirmVerificationCodeAndDeleteInRedis(appCustomerSignupRequestDto.getPhoneNumber(),
                appCustomerSignupRequestDto.getVerificationCode());

        User user = User.builder()
                .accountId(appCustomerSignupRequestDto.getAccountId())
                .password(bCryptPasswordEncoder.encode(appCustomerSignupRequestDto.getPassword()))
                .phoneNumber(appCustomerSignupRequestDto.getPhoneNumber())
                .privacyConsent(appCustomerSignupRequestDto.getPrivacyConsent())
                .marketingConsent(appCustomerSignupRequestDto.getMarketingConsent())
                .roleType(RoleType.CUSTOMER)
                .build();

        String profileImageFileUrl = imageFileService.uploadImageFile(S3Folder.CUSTOMER_PROFILE_IMAGE, profileImageFile);

        user.setCustomer(Customer.builder()
                .nickname(appCustomerSignupRequestDto.getNickname())
                .profileImageUrl(profileImageFileUrl)
                .build());

        userRepository.save(user);
    }

    /**
     * App 계정 사장님 타입 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleAppOwnerSignup(AppOwnerSignupRequestDto appOwnerSignupRequestDto,
                                     MultipartFile storeProfileImageFile,
                                     MultipartFile storeFeaturedImageFile,
                                     List<MultipartFile> storeImageFileList) {

        confirmVerificationCodeAndDeleteInRedis(appOwnerSignupRequestDto.getPhoneNumber(),
                appOwnerSignupRequestDto.getVerificationCode());

        String storeProfileImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_PROFILE_IMAGE, storeProfileImageFile);
        String storeFeaturedImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_IMAGE, storeFeaturedImageFile);

        User user = User.builder()
                .accountId(appOwnerSignupRequestDto.getAccountId())
                .password(bCryptPasswordEncoder.encode(appOwnerSignupRequestDto.getPassword()))
                .phoneNumber(appOwnerSignupRequestDto.getPhoneNumber())
                .privacyConsent(appOwnerSignupRequestDto.getPrivacyConsent())
                .marketingConsent(appOwnerSignupRequestDto.getMarketingConsent())
                .roleType(RoleType.OWNER)
                .build();

        Store store = Store.builder()
                .name(appOwnerSignupRequestDto.getStoreName())
                .profileImageUrl(storeProfileImageFileUrl)
                .featuredImageUrl(storeFeaturedImageFileUrl)
                .description(appOwnerSignupRequestDto.getStoreDescription())
                .category(appOwnerSignupRequestDto.getStoreCategory())
                .detailCategory(appOwnerSignupRequestDto.getStoreDetailCategory())
                .location(appOwnerSignupRequestDto.getStoreLocation())
                .locationCode(appOwnerSignupRequestDto.getStoreLocationCode())
                .locationDetail(appOwnerSignupRequestDto.getStoreLocationDetail())
                .lat(appOwnerSignupRequestDto.getStoreLat())
                .lng(appOwnerSignupRequestDto.getStoreLng())
                .phoneNumber(appOwnerSignupRequestDto.getStorePhoneNumber())
                .intro(appOwnerSignupRequestDto.getStoreIntro())
                .build();

        user.addStore(store);

        if(!ObjectUtils.isEmpty(storeImageFileList) && !storeImageFileList.isEmpty()){
            List<String> uploadedUrlList = imageFileService.uploadImageFileList(S3Folder.STORE_IMAGE, storeImageFileList);
            List<StoreImage> storeImageList = IntStream.range(0, storeImageFileList.size())
                    .mapToObj(index -> StoreImage.builder()
                            .imageUrl(uploadedUrlList.get(index))
                            .order(index)
                            .build())
                    .toList();

            store.addStoreImageList(storeImageList);
        }

        userRepository.save(user);
    }

    /**
     * Kakao 계정 손님타입 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleKakaoCustomerSignup(KakaoCustomerSignupRequestDto kakaoCustomerSignupRequestDto,
                                          MultipartFile profileImageFile) {

        confirmVerificationCodeAndDeleteInRedis(kakaoCustomerSignupRequestDto.getPhoneNumber(),
                kakaoCustomerSignupRequestDto.getVerificationCode());

        User user = User.builder()
                .kakaoId(kakaoCustomerSignupRequestDto.getKakaoId())
                .phoneNumber(kakaoCustomerSignupRequestDto.getPhoneNumber())
                .privacyConsent(kakaoCustomerSignupRequestDto.getPrivacyConsent())
                .marketingConsent(kakaoCustomerSignupRequestDto.getMarketingConsent())
                .roleType(RoleType.CUSTOMER)
                .build();

        String profileImageFileUrl = imageFileService.uploadImageFile(S3Folder.CUSTOMER_PROFILE_IMAGE, profileImageFile);

        user.setCustomer(Customer.builder()
                .nickname(kakaoCustomerSignupRequestDto.getNickname())
                .profileImageUrl(profileImageFileUrl)
                .build());

        userRepository.save(user);
    }

    /**
     * Kakao 계정 사장님 타입 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleKakaoOwnerSignup(KakaoOwnerSignupRequestDto kakaoOwnerSignupRequestDto,
                                       MultipartFile storeProfileImageFile,
                                       MultipartFile storeFeaturedImageFile,
                                       List<MultipartFile> storeImageFileList) {

        confirmVerificationCodeAndDeleteInRedis(kakaoOwnerSignupRequestDto.getPhoneNumber(),
                kakaoOwnerSignupRequestDto.getVerificationCode());

        String storeProfileImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_PROFILE_IMAGE, storeProfileImageFile);
        String storeFeaturedImageFileUrl = imageFileService.uploadImageFile(S3Folder.STORE_IMAGE, storeFeaturedImageFile);

        User user = User.builder()
                .kakaoId(kakaoOwnerSignupRequestDto.getKakaoId())
                .phoneNumber(kakaoOwnerSignupRequestDto.getPhoneNumber())
                .privacyConsent(kakaoOwnerSignupRequestDto.getPrivacyConsent())
                .marketingConsent(kakaoOwnerSignupRequestDto.getMarketingConsent())
                .roleType(RoleType.OWNER)
                .build();

        Store store = Store.builder()
                .name(kakaoOwnerSignupRequestDto.getStoreName())
                .profileImageUrl(storeProfileImageFileUrl)
                .featuredImageUrl(storeFeaturedImageFileUrl)
                .description(kakaoOwnerSignupRequestDto.getStoreDescription())
                .category(kakaoOwnerSignupRequestDto.getStoreCategory())
                .detailCategory(kakaoOwnerSignupRequestDto.getStoreDetailCategory())
                .location(kakaoOwnerSignupRequestDto.getStoreLocation())
                .locationCode(kakaoOwnerSignupRequestDto.getStoreLocationCode())
                .locationDetail(kakaoOwnerSignupRequestDto.getStoreLocationDetail())
                .lat(kakaoOwnerSignupRequestDto.getStoreLat())
                .lng(kakaoOwnerSignupRequestDto.getStoreLng())
                .phoneNumber(kakaoOwnerSignupRequestDto.getStorePhoneNumber())
                .intro(kakaoOwnerSignupRequestDto.getStoreIntro())
                .build();

        user.addStore(store);

        if(!ObjectUtils.isEmpty(storeImageFileList) && !storeImageFileList.isEmpty()){
            List<String> uploadedUrlList = imageFileService.uploadImageFileList(S3Folder.STORE_IMAGE, storeImageFileList);
            List<StoreImage> storeImageList = IntStream.range(0, storeImageFileList.size())
                    .mapToObj(index -> StoreImage.builder()
                            .imageUrl(uploadedUrlList.get(index))
                            .order(index)
                            .build())
                    .toList();

            store.addStoreImageList(storeImageList);
        }

        userRepository.save(user);
    }

    /**
     * App 계정 연동 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleAppLinkSignup(AppLinkSignupRequestDto appLinkSignupRequestDto) {

        confirmVerificationCodeAndDeleteInRedis(appLinkSignupRequestDto.getPhoneNumber(),
                appLinkSignupRequestDto.getVerificationCode());

        User user = userRepository.findByPhoneNumber(appLinkSignupRequestDto.getPhoneNumber()).orElseThrow(() -> {
                    log.error("User not found with phone number: {}", appLinkSignupRequestDto.getPhoneNumber());
                    return new UserException(ErrorStatus._BAD_REQUEST);
                }
        );

        user.setAccountId(appLinkSignupRequestDto.getAccountId());
        user.setPassword(bCryptPasswordEncoder.encode(appLinkSignupRequestDto.getPassword()));
        user.setRoleType(RoleType.DUAL);
    }

    /**
     * App 계정 연동 회원가입 요청을 처리하는 메서드
     */
    @Transactional
    public void handleKakaoLinkSignup(KakaoLinkSignupRequestDto kakaoLinkSignupRequestDto) {

        confirmVerificationCodeAndDeleteInRedis(kakaoLinkSignupRequestDto.getPhoneNumber(),
                kakaoLinkSignupRequestDto.getVerificationCode());

        User user = userRepository.findByPhoneNumber(kakaoLinkSignupRequestDto.getPhoneNumber()).orElseThrow(() -> {
                    log.error("User not found with phone number: {}", kakaoLinkSignupRequestDto.getPhoneNumber());
                    return new UserException(ErrorStatus._BAD_REQUEST);
                }
        );

        user.setKakaoId(kakaoLinkSignupRequestDto.getKakaoId());
        user.setRoleType(RoleType.DUAL);
    }

    /**
     * App 계정 Id 중복검사 요청을 처리하는 메서드
     */
    @Transactional(readOnly = true)
    public AccountIdCheckResponseDto checkAccountId(AccountIdCheckRequestDto accountIdCheckRequestDto){

        boolean isDuplicated = userRepository.existsByAccountId(accountIdCheckRequestDto.getAccountId());

        return AccountIdCheckResponseDto.builder()
                .isDuplicated(isDuplicated)
                .build();
    }

    /**
     * 회원가입 모드 조회 요청을 처리하는 메서드
     */
    @Transactional(readOnly = true)
    public SignupModeResponseDto getSignupMode(SignupModeRequestDto signupModeRequestDto){
        Optional<User> optionalUser = userRepository.findByPhoneNumber(signupModeRequestDto.getPhoneNumber());

        // 회원가입한 적이 없으로 다음 회원가입 모드는 NORMAL_SIGNUP
        if(optionalUser.isEmpty()){
            return new SignupModeResponseDto(SignupMode.NORMAL_SIGNUP);
        }
        else{
            User user = optionalUser.get();
            switch(signupModeRequestDto.getSignupType()){
                case APP:
                    // App 계정으로 회원가입한 적이 없으므로 다음 회원가입 모드는 LINK_SIGNUP
                    if(user.getAccountId()==null)
                        return new SignupModeResponseDto(SignupMode.LINK_SIGNUP);
                    break;

                case KAKAO:
                    // Kakao 계정으로 회원가입한 적이 없으므로 다음 회원가입 모드는 LINK_SIGNUP
                    if(user.getKakaoId()==null)
                        return new SignupModeResponseDto(SignupMode.LINK_SIGNUP);
                    break;

                default:
                    throw new UserException(ErrorStatus._BAD_REQUEST);
            }
        }

        // 이미 해당 계정으로 회원가입 했으므로 ALREADY_SIGNED_UP을 반환
        stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                signupModeRequestDto.getPhoneNumber());
        return new SignupModeResponseDto(SignupMode.ALREADY_SIGNED_UP);
    }

    /**
     * 인증 코드가 유효한지 검사하고 유효하다면 인증코드를 redis에서 삭제하는 메서드
     */
    public void confirmVerificationCodeAndDeleteInRedis(String phoneNumber, String verificationCode) {
        // 인증 코드가 유효한지 검사
        verificationService.checkIfVerificationCodeConfirmed(phoneNumber, verificationCode);

        // 유효성 검사가 끝난 인증 코드를 redis에서 삭제
        stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() + phoneNumber);
    }

}

