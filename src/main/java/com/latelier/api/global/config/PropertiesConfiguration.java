package com.latelier.api.global.config;

import com.latelier.api.global.properties.ZoomProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {ZoomProperties.class})
public class PropertiesConfiguration {
}
