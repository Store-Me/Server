package com.example.storeme.global.config.nurigo;

import com.example.storeme.global.config.properties.SmsProperties;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DefaultMessageServiceConfig {

    private final SmsProperties smsProperties;

    @Bean
    public DefaultMessageService defaultMessageService() {
        return NurigoApp.INSTANCE.initialize(
                smsProperties.getKey(), smsProperties.getSecret(), smsProperties.getUrl());
    }
}
