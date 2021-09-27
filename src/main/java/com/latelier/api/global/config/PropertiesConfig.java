package com.latelier.api.global.config;

import com.latelier.api.global.properties.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
        value = {
                ZoomProperties.class,
                NaverProperties.class,
                AppProperties.class,
                JwtProperties.class,
                CloudProperties.class,
                IamportProperties.class})
public class PropertiesConfig {
}
