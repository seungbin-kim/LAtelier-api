package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.exception.PhoneNumberDuplicateException;
import com.latelier.api.domain.member.repository.MemberRepository;
import com.latelier.api.domain.member.repository.SmsCertificationRepository;
import com.latelier.api.domain.util.SignatureGenerator;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.properties.NaverProperties;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SmsService {

    private final MemberRepository memberRepository;

    private final SmsCertificationRepository smsCertificationRepository;

    private final NaverProperties naverProperties;

    private final SignatureGenerator signatureGenerator;

    private final RestTemplate restTemplate;


    /**
     * 인증번호를 만들어 사용자에게 전송후 Redis 저장
     *
     * @param phoneNumber 수신 휴대폰번호
     */
    @Transactional(rollbackFor = Throwable.class)
    public void sendCertificationNumber(final String phoneNumber) {

        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNumberDuplicateException(phoneNumber);
        }

        int randomNumber = getRandomNumber();
        ReqSms smsRequest = makeSmsRequestForVerification(phoneNumber, randomNumber);
        callApi(smsRequest);

        smsCertificationRepository.saveSmsCertification(phoneNumber, randomNumber);
    }


    /**
     * 개별 메세지 전송을 위한 요청을 만듭니다.
     *
     * @param toAndContent Map<수신번호, 내용>
     * @return 메세지 전송 요청객체
     */
    public ReqSms makeSmsRequest(final Map<String, String> toAndContent) {

        String from = naverProperties.getCloudPlatform().getSens().getSmsFrom();

        List<ReqSms.Message> messages = new ArrayList<>();
        for (Map.Entry<String, String> entry : toAndContent.entrySet()) {

            ReqSms.Message message = ReqSms.Message.ofEach(entry.getKey(), entry.getValue());
            messages.add(message);
        }

        return ReqSms.of(from, "[Latelier]",messages);
    }


    /**
     * Redis 에 저장된 인증번호와 사용자가 입력한 인증번호 일치여부 확인
     *
     * @param phoneNumber         휴대폰번호
     * @param certificationNumber 인증번호
     */
    @Transactional(rollbackFor = Throwable.class)
    public void verifySMS(final String phoneNumber, final String certificationNumber) {

        if (!isVerify(phoneNumber, certificationNumber)) {
            throw new BusinessException(ErrorCode.SMS_VERIFICATION_FAILED);
        }
        smsCertificationRepository.removeSmsCertification(phoneNumber);
    }


    private boolean isVerify(final String phoneNumber, final String certificationNumber) {

        if (smsCertificationRepository.hasKey(phoneNumber)) {
            return smsCertificationRepository.getSmsCertification(phoneNumber).equals(certificationNumber);
        }
        return false;
    }


    /**
     * 인증번호 전송을 위한 요청 생성
     *
     * @param phoneNumber  수신 휴대폰번호
     * @param randomNumber 랜덤으로 생성된 번호
     * @return 6자리의 랜덤 인증번호가 설정된 요청
     */
    private ReqSms makeSmsRequestForVerification(final String phoneNumber, final int randomNumber) {

        String from = naverProperties.getCloudPlatform().getSens().getSmsFrom();

        List<ReqSms.Message> messages = new ArrayList<>();
        messages.add(ReqSms.Message.of(phoneNumber));

        String content = "[Latelier] 인증번호는 [" + randomNumber + "] 입니다.";
        return ReqSms.of(from, content, messages);
    }


    /**
     * 네이버 SENS API 호출
     *
     * @param smsRequest SMS 요청 객체
     */
    public void callApi(final ReqSms smsRequest) {

        final String time = Long.toString(System.currentTimeMillis());
        final String serviceId = naverProperties.getCloudPlatform().getSens().getServiceId();
        final String signature = signatureGenerator.generateSignatureForSms(time);
        final String url = naverProperties.getCloudPlatform().getSens().getUrl() + serviceId + "/messages";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-ncp-apigw-timestamp", time);
        httpHeaders.set("x-ncp-iam-access-key", naverProperties.getCloudPlatform().getKey());
        httpHeaders.set("x-ncp-apigw-signature-v2", signature);

        HttpEntity<ReqSms> body = new HttpEntity<>(smsRequest, httpHeaders);

        try {
            restTemplate.postForObject(
                    url,
                    body,
                    ResSms.class);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SMS_API_CALL_FAILED);
        }
    }


    private int getRandomNumber() {

        return (int) (Math.random() * 900000) + 100000;
    }


    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ReqSms {

        private final String type;

        private final String from;

        private final String content;

        private final List<Message> messages;

        private String countryCode;

        private String contentType;


        public static ReqSms of(final String from, final String content, final List<Message> messages) {

            return new ReqSms("SMS", from, content, messages);
        }

        @Getter
        @Setter
        @EqualsAndHashCode
        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        static class Message {

            private final String to;

            private String content;


            private Message(String to, String content) {
                this.to = to;
                this.content = content;
            }

            public static Message of(final String to) {

                return new Message(to);
            }

            public static Message ofEach(final String to, final String content) {

                return new Message(to, content);
            }

        }

    }


    @Getter
    static class ResSms {

        private String requestId;

        private String requestTime;

        private String statusCode;

        private String statusName;

    }

}