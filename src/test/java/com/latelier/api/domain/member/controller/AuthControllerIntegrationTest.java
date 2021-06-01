package com.latelier.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latelier.api.domain.member.packet.request.ReqSmsAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class AuthControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;


  @DisplayName("인증문자_보내기")
  @Test
  void sendSms() throws Exception {
    //given
    ReqSmsAuthentication req = new ReqSmsAuthentication();
    ReflectionTestUtils.setField(req, "phoneNumber", "01074787389");
    ObjectMapper objectMapper = new ObjectMapper();
    String content = objectMapper.writeValueAsString(req);

    //when
    ResultActions perform = mockMvc.perform(post("/auth/sms")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content));

    //then
    perform
        .andExpect(status().isAccepted())
        .andDo(print());
  }

}