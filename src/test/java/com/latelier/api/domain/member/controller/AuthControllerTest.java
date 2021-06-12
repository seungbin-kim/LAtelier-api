package com.latelier.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latelier.api.domain.member.packet.request.ReqSmsAuthentication;
import com.latelier.api.domain.member.packet.request.ReqSmsVerification;
import com.latelier.api.domain.member.repository.SmsCertificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ApplicationContext context;

  @Autowired
  ObjectMapper objectMapper;


  @Test
  @DisplayName("SMS_보내기")
  void sendSms(@Value("${test.phoneNumber}") String phoneNumber) throws Exception {
    // given
    ReqSmsAuthentication req = new ReqSmsAuthentication();
    ReflectionTestUtils.setField(req, "phoneNumber", phoneNumber);
    String content = objectMapper.writeValueAsString(req);

    // when
    ResultActions perform = mockMvc.perform(post("/auth/sms")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content));

    // then
    perform
        .andExpect(status().isAccepted())
        .andDo(print());
  }


  @Test
  @DisplayName("SMS_인증확인")
  void verifySms(@Value("${test.phoneNumber}") String phoneNumber) throws Exception {
    // given
    String certificationNumber = "123456";
    SmsCertificationRepository repository = context.getBean(SmsCertificationRepository.class);
    repository.saveSmsCertification(phoneNumber, Integer.parseInt(certificationNumber));

    ReqSmsVerification req = new ReqSmsVerification();
    ReflectionTestUtils.setField(req, "phoneNumber", phoneNumber);
    ReflectionTestUtils.setField(req, "certificationNumber", certificationNumber);
    String content = objectMapper.writeValueAsString(req);

    // when
    ResultActions perform = mockMvc.perform(post("/auth/sms/verification")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content));

    // then
    perform
        .andExpect(status().isOk())
        .andDo(print());
  }

}