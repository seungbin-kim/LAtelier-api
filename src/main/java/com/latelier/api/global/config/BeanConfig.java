package com.latelier.api.global.config;

import com.latelier.api.global.properties.NaverProperties;
import com.latelier.api.global.properties.ZoomProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final ZoomProperties zoomProperties;

    private final NaverProperties naverProperties;


    @Bean
    public SecretKeySpec sdkSigningKey() {
        return new SecretKeySpec(zoomProperties
                .getApi()
                .getSecret()
                .getBytes(), "HmacSHA256");
    }


    @Bean
    public SecretKeySpec smsSigningKey() {
        return new SecretKeySpec(naverProperties
                .getCloudPlatform()
                .getSecret()
                .getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

}
