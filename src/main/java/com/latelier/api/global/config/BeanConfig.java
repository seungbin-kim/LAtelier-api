package com.latelier.api.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.latelier.api.global.properties.CloudProperties;
import com.latelier.api.global.properties.NaverProperties;
import com.latelier.api.global.properties.ZoomProperties;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final ZoomProperties zoomProperties;

    private final NaverProperties naverProperties;

    private final CloudProperties cloudProperties;


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


    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials =
                new BasicAWSCredentials(
                        cloudProperties.getAws().getCredentials().getKey(),
                        cloudProperties.getAws().getCredentials().getSecret());

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(cloudProperties.getAws().getRegion().getStaticValue())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }


    @Bean
    public Tika tika() {
        return new Tika();
    }

}
