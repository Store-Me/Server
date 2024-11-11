package com.example.storeme.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class StringRedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    // 특정 키에 대한 값이 있는지 확인
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    // key 에 해당하는 데이터 얻어오는 메서드
    public String getData(String key) {
        return getStringStringValueOperations().get(key);
    }

    // key - value 데이터 설정하는 메서드
    public void setData(String key, String value) {
        getStringStringValueOperations().set(key, value);
    }

    // key 에 해당하는 데이터 삭제하는 메서드
    public void deleteData(String key) {
        this.stringRedisTemplate.delete(key);
    }

    // key 에 해당하는 데이터 만료기간 설정 메서드
    public void setDataExpire(String key, String value, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        getStringStringValueOperations().set(key, value, expireDuration);
    }

    // 특정 시간까지 데이터가 유지되도록 설정하는 메서드
    public void setDataExpireAt(String key, String value, LocalDateTime expiryTime) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간과 특정 시간 사이의 차이를 계산
        long secondsUntilExpiry = ChronoUnit.SECONDS.between(now, expiryTime);

        // 데이터 설정 및 만료 시간 설정
        setDataExpire(key, value, secondsUntilExpiry);
    }

    // Redis에 저장되어 있는 값에 ttl을 설정하는 메서드
    public void setExpire(String key, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        stringRedisTemplate.expire(key, expireDuration);
    }

    // 데이터의 값을 1만큼 증가시키는 메서드
    public long incrementData(String key) {
        Long result = getStringStringValueOperations().increment(key, 1);
        if(result == null) {
            log.error("Increment operation failed in StringRedisUtil");
            throw new IllegalArgumentException("Increment operation failed in StringRedisUtil");
        }
        return result;
    }

    private ValueOperations<String, String> getStringStringValueOperations() {
        return this.stringRedisTemplate.opsForValue();
    }
}
