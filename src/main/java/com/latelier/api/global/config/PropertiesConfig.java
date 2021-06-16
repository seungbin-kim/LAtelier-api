package com.latelier.api.global.config;

import com.latelier.api.global.properties.AppProperties;
import com.latelier.api.global.properties.JwtProperties;
import com.latelier.api.global.properties.NaverProperties;
import com.latelier.api.global.properties.ZoomProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
    value = {
        ZoomProperties.class,
        NaverProperties.class,
        AppProperties.class,
        JwtProperties.class})
public class PropertiesConfig {
}
