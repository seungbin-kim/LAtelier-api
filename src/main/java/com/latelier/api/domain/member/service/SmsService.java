package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.packet.ReqSms;
import com.latelier.api.domain.member.packet.ResSms;
import com.latelier.api.domain.util.SignatureGenerator;
import com.latelier.api.global.properties.NaverProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

  private final NaverProperties naverProperties;

  private final SignatureGenerator signatureGenerator;

  private final RestTemplate restTemplate;


  public void sendCertificationNumber(final String phoneNumber) {

    String time = Long.toString(System.currentTimeMillis());

    String from = naverProperties.getCloudPlatform().getSens().getSmsFrom();
    String key = naverProperties.getCloudPlatform().getKey();
    String serviceId = naverProperties.getCloudPlatform().getSens().getServiceId();
    String url = naverProperties.getCloudPlatform().getSens().getUrl() + serviceId + "/messages";
    String signature = signatureGenerator.generateSignatureForSms(time);

    List<ReqSms.Message> messages = new ArrayList<>();
    messages.add(ReqSms.Message.createMessage(phoneNumber));

    int certificationNumber = getRandomNumber();
    String content = "[Latelier] 인증번호는 [" + certificationNumber + "] 입니다.";
    ReqSms smsRequest = ReqSms.createSmsRequest(from, content, messages);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.set("x-ncp-apigw-timestamp", time);
    httpHeaders.set("x-ncp-iam-access-key", key);
    httpHeaders.set("x-ncp-apigw-signature-v2", signature);

    HttpEntity<ReqSms> body = new HttpEntity<>(smsRequest, httpHeaders);

    ResSms resSms = restTemplate.postForObject(
        url,
        body,
        ResSms.class);
    log.info("문자 전송 응답: {}", resSms);
  }


  private int getRandomNumber() {
    return (int) (Math.random() * 900000) + 100000;
  }

}
