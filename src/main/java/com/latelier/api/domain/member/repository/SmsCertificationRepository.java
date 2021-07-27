package com.latelier.api.domain.member.repository;

import com.latelier.api.global.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class SmsCertificationRepository {

    private final AppProperties appProperties;

    private final StringRedisTemplate stringRedisTemplate;

    private String PREFIX;

    private long LIMIT_TIME;


    @PostConstruct
    public void init() {
        PREFIX = appProperties.getSms().getRedisPrefix();
        LIMIT_TIME = appProperties.getSms().getExpireTime();
    }


    public void saveSmsCertification(final String phoneNumber,
                                     final int certificationNumber) {

        stringRedisTemplate.opsForValue()
                .set(PREFIX + phoneNumber, Integer.toString(certificationNumber), Duration.ofMinutes(LIMIT_TIME));
    }


    public String getSmsCertification(final String phoneNumber) {

        return stringRedisTemplate.opsForValue().get(PREFIX + phoneNumber);
    }


    public void removeSmsCertification(final String phoneNumber) {

        stringRedisTemplate.delete(PREFIX + phoneNumber);
    }


    public boolean hasKey(final String phoneNumber) {

        return stringRedisTemplate.hasKey(PREFIX + phoneNumber);
    }

}
