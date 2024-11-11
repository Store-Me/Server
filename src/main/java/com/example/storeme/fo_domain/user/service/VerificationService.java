package com.example.storeme.fo_domain.user.service;

import com.example.storeme.fo_domain.user.constant.SignupType;
import com.example.storeme.fo_domain.user.constant.VerificationProperty;
import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.fo_domain.user.dto.verification.ConfirmCodeRequestDto;
import com.example.storeme.fo_domain.user.dto.verification.ConfirmCodeResponseDto;
import com.example.storeme.fo_domain.user.dto.verification.VerificationCodeRequestDto;
import com.example.storeme.fo_domain.user.dto.verification.VerificationCodeResponseDto;
import com.example.storeme.fo_domain.user.exception.UserException;
import com.example.storeme.fo_domain.user.repository.UserRepository;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.constant.RedisKeyPrefix;
import com.example.storeme.global.config.properties.SmsProperties;
import com.example.storeme.global.util.RandomCodeUtil;
import com.example.storeme.global.util.StringRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 휴대폰 인증 기능을 수행하는 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final DefaultMessageService messageService;
    private final StringRedisUtil stringRedisUtil;
    private final RandomCodeUtil randomCodeUtil;
    private final SmsProperties smsProperties;
    private final UserRepository userRepository;

    /**
     * 인증 코드를 발급하는 메서드
     * <p>
     * 1. CoolSms를 사용하여 인증코드를 발급하고 인증 제한시간을 응답에 담아 반환한다.
     * 2. 인증 코드 발급 제한 횟수를 초과하면 내일 다시 인증하라는 응답을 전송한다.
     */
    public VerificationCodeResponseDto sendVerificationCode(VerificationCodeRequestDto verificationCodeRequestDto) {

        // 인증코드 발급이 처음이면 redis에 발급 횟수를 저장(유효 기간: 밤 12시 전까지)
        if (!stringRedisUtil.hasKey(RedisKeyPrefix.VERIFICATION_ISSUE_COUNT.getPrefix() +
                verificationCodeRequestDto.getPhoneNumber())) {
            stringRedisUtil.setDataExpireAt(RedisKeyPrefix.VERIFICATION_ISSUE_COUNT.getPrefix() +
                            verificationCodeRequestDto.getPhoneNumber(),
                    String.valueOf(1), LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));

        }
        // 인증코드 발급 제한 횟수를 초과하면 예외 발생
        else {
            long issueCount = stringRedisUtil.incrementData(RedisKeyPrefix.VERIFICATION_ISSUE_COUNT.getPrefix() +
                    verificationCodeRequestDto.getPhoneNumber());
            if (issueCount > VerificationProperty.MAX_CODE_ISSUE_ATTEMPTS.getValue()) {
                log.error("Exceeded the number of code issuance attempts.");
                throw new UserException(ErrorStatus._AUTH_CODE_ISSUE_LIMIT_EXCEEDED);
            }
        }

        // 인증코드의 인증 횟수 삭제 (초기화 기능)
        stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_ATTEMPTS.getPrefix() +
                verificationCodeRequestDto.getPhoneNumber());

        Message message = new Message();
        message.setFrom(smsProperties.getSenderNumber());
        message.setTo(verificationCodeRequestDto.getPhoneNumber());

        String verificationCode = randomCodeUtil.generateRandomCode(
                VerificationProperty.CODE_LENGTH.getValue());
        message.setText("[Store Me] 본인 확인 인증번호는 (" + verificationCode + ") 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("Verification code sent to {} {}", verificationCodeRequestDto.getPhoneNumber(), response);

        // 인증코드 저장(유효시간 설정)
        stringRedisUtil.setDataExpire(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                        verificationCodeRequestDto.getPhoneNumber(),
                        verificationCode,
                        VerificationProperty.TIME_LIMIT.getValue());

        return VerificationCodeResponseDto.builder()
                .timeLimit(VerificationProperty.TIME_LIMIT.getValue())
                .build();
    }

    /**
     * 인증 코드가 올바른지 검증하는 메서드
     * <p>
     * 1. 인증 코드를 검증하여 Redis에 있는 인증코드와 같은지를 검사한다.
     * 2. 제한시간이 지났거나 인증코드 불일치, 혹은 인증 제한 횟수를 초과한 경우 예외를 던진다.
     * 3. 인증 코드가 유효한지 검사하여 유효하지 않으면 예외를 던진다.
     * 4. 다음에 client가 요청해야 할 회원가입 유형을 생성하여 응답 Dto에 담아 반환한다.
     */
    public ConfirmCodeResponseDto confirmVerificationCode(ConfirmCodeRequestDto confirmCodeRequestDto) {

        // 인증코드의 인증 제한 횟수를 초과하면 예외 발생
        if (stringRedisUtil.hasKey(RedisKeyPrefix.VERIFICATION_ATTEMPTS.getPrefix() +
                confirmCodeRequestDto.getPhoneNumber())) {
            long attemptCount = stringRedisUtil.incrementData(RedisKeyPrefix.VERIFICATION_ATTEMPTS.getPrefix() +
                    confirmCodeRequestDto.getPhoneNumber());
            if (attemptCount > VerificationProperty.MAX_CODE_CONFIRM_ATTEMPTS.getValue()) {
                log.error("Verification code attempts exceeded.");
                throw new UserException(ErrorStatus._AUTH_CODE_ATTEMPTS_EXCEEDED);
            }
        }
        // 인증코드의 인증 횟수 설정(유효 기간: 밤 12시 전까지)
        else {
            stringRedisUtil.setDataExpireAt(RedisKeyPrefix.VERIFICATION_ATTEMPTS.getPrefix() +
                            confirmCodeRequestDto.getPhoneNumber(),
                    String.valueOf(1), LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));
        }

        // 인증 코드가 유효한지 검사
        checkIfVerificationCodeConfirmed(confirmCodeRequestDto.getPhoneNumber(),
                confirmCodeRequestDto.getVerificationCode());

        // client가 요청해야 할 회원가입 유형을 응답 Dto에 담아 반환한다.
        return new ConfirmCodeResponseDto(getSignupMode(confirmCodeRequestDto));

    }

    // 다음에 client가 요청해야 할 회원가입 유형을 반환한다.
    private ConfirmCodeResponseDto.SignupMode getSignupMode(ConfirmCodeRequestDto confirmCodeRequestDto) {
        stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_ISSUE_COUNT.getPrefix() +
                confirmCodeRequestDto.getPhoneNumber());
        stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_ATTEMPTS.getPrefix() +
                confirmCodeRequestDto.getPhoneNumber());

        Optional<User> optionalUser = userRepository.findByPhoneNumber(confirmCodeRequestDto.getPhoneNumber());
        if(optionalUser.isEmpty()){
            // 인증 성공 응답 => 정상적인 회원가입
            // redis에 code 값 대신 CONFIRMED 넣고 TTL 1시간으로 설정
            stringRedisUtil.setExpire(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                            confirmCodeRequestDto.getPhoneNumber(),
                    VerificationProperty.CONFIRMED_CODE_TIME_LIMIT.getValue());

            return ConfirmCodeResponseDto.SignupMode.NORMAL_SIGNUP;
        }
        else{
            User user = optionalUser.get();
            switch(confirmCodeRequestDto.getSignupType()){
                case APP:
                    if(user.getAccountId()==null){
                        stringRedisUtil.setExpire(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                                        confirmCodeRequestDto.getPhoneNumber(),
                                VerificationProperty.CONFIRMED_CODE_TIME_LIMIT.getValue());

                        return ConfirmCodeResponseDto.SignupMode.LINK_SIGNUP;
                    }
                    stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                            confirmCodeRequestDto.getPhoneNumber());
                    return ConfirmCodeResponseDto.SignupMode.ALREADY_SIGNED_UP;

                case KAKAO:
                    if(user.getKakaoId()==null){
                        stringRedisUtil.setExpire(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                                        confirmCodeRequestDto.getPhoneNumber(),
                                VerificationProperty.CONFIRMED_CODE_TIME_LIMIT.getValue());

                        return ConfirmCodeResponseDto.SignupMode.LINK_SIGNUP;
                    }
                    stringRedisUtil.deleteData(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                            confirmCodeRequestDto.getPhoneNumber());
                    return ConfirmCodeResponseDto.SignupMode.ALREADY_SIGNED_UP;
                default:
                    return ConfirmCodeResponseDto.SignupMode.NORMAL_SIGNUP;
            }
        }
    }

    /**
     * 인증번호가 Redis에 저장된 인증번호와 같은지 검사하는 메서드
     * @param phoneNumber
     * @param verificationCode
     */
    public void checkIfVerificationCodeConfirmed(String phoneNumber, String verificationCode){
        String originalVerificationCode = stringRedisUtil.getData(RedisKeyPrefix.VERIFICATION_CODE.getPrefix() +
                phoneNumber);

        if (originalVerificationCode == null) {
            log.error("Verification code has expired.");
            throw new UserException(ErrorStatus._AUTH_CODE_NOT_EXIST);
        }

        if (!originalVerificationCode.equals(verificationCode)) {
            log.error("Verification code does not match. original code: {}, code sent: {}",
                    originalVerificationCode, verificationCode);
            throw new UserException(ErrorStatus._AUTH_CODE_NOT_MATCH);
        }
    }
}
