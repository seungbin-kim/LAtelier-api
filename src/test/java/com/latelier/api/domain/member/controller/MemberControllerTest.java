package com.latelier.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;


  @DisplayName("회원등록_성공")
  @ParameterizedTest(name = "{index} {displayName} name={0}")
  @CsvSource({
      "홍길동, 01011111111, test1@a.b, myNickname1, !myPassword486@",
      "홍길순, 01000000000, test2@a.b, myNickname2, !!myPassword486@"})
  void signUp(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
    // given
    String content = objectMapper.writeValueAsString(req);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content)
        .accept(MediaType.APPLICATION_JSON));

    // then
    perform
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.name").value(req.getName()))
        .andExpect(jsonPath("$.content.phoneNumber").value(req.getPhoneNumber()))
        .andExpect(jsonPath("$.content.email").value(req.getEmail()))
        .andExpect(jsonPath("$.content.nickname").value(req.getNickname()))
        .andDo(print());
  }

  static class SignUpRequestAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

      ReqSignUp reqSignUp = new ReqSignUp();
      ReflectionTestUtils.setField(reqSignUp, "name", accessor.getString(0));
      ReflectionTestUtils.setField(reqSignUp, "phoneNumber", accessor.getString(1));
      ReflectionTestUtils.setField(reqSignUp, "email", accessor.getString(2));
      ReflectionTestUtils.setField(reqSignUp, "nickname", accessor.getString(3));
      ReflectionTestUtils.setField(reqSignUp, "password", accessor.getString(4));
      return reqSignUp;
    }
  }

}