package com.latelier.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latelier.api.domain.member.packet.request.ReqSignIn;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.request.ReqSmsAuthentication;
import com.latelier.api.domain.member.packet.request.ReqSmsVerification;
import com.latelier.api.domain.member.repository.SmsCertificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
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

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("SMS_보내기")
    void sendSms(@Value("${test.phoneNumber}") String phoneNumber) throws Exception {
        // given
        ReqSmsAuthentication req = new ReqSmsAuthentication();
        ReflectionTestUtils.setField(req, "phoneNumber", phoneNumber);
        String content = objectMapper.writeValueAsString(req);

        // when
        ResultActions perform = mockMvc.perform(post("/api/auth/sms")
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
        ResultActions perform = mockMvc.perform(post("/api/auth/sms/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("회원등록후_로그인_성공")
    @ParameterizedTest(name = "[{index}] name={0}, phoneNumber={1}, email={2}, isTeacher={4}")
    @CsvSource({
            "홍길동, 01011111111, test1@a.b, !myPassword486@, true",
            "홍길순, 01022222222, test2@a.b, !myPassword486@, false"})
    void signIn(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        registerRole();

        String signUpRequest = objectMapper.writeValueAsString(req);

        ReqSignIn reqSignIn = new ReqSignIn();
        ReflectionTestUtils.setField(reqSignIn, "email", req.getEmail());
        ReflectionTestUtils.setField(reqSignIn, "password", req.getPassword());
        String content = objectMapper.writeValueAsString(reqSignIn);

        // when
        ResultActions signUp = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest));

        ResultActions perform = mockMvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        signUp
                .andExpect(status().isCreated())
                .andDo(print());

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.token").isNotEmpty())
                .andDo(print());
    }


    @DisplayName("회원등록후_로그인_실패(이메일)")
    @ParameterizedTest(name = "[{index}] name={0}, phoneNumber={1}, email={2}, isTeacher={4}")
    @CsvSource({
            "홍길동, 01011111111, test1@a.b, !myPassword486@, true",
            "홍길순, 01022222222, test2@a.b, !myPassword486@, false"})
    void signInFailEmail(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        registerRole();

        String signUpRequest = objectMapper.writeValueAsString(req);

        ReqSignIn reqSignIn = new ReqSignIn();
        ReflectionTestUtils.setField(reqSignIn, "email", "wrongPassword");
        ReflectionTestUtils.setField(reqSignIn, "password", req.getPassword());
        String content = objectMapper.writeValueAsString(reqSignIn);

        // when
        ResultActions signUp = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest));

        ResultActions perform = mockMvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        signUp
                .andExpect(status().isCreated())
                .andDo(print());

        perform
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @DisplayName("회원등록후_로그인_실패(비밀번호)")
    @ParameterizedTest(name = "[{index}] name={0}, phoneNumber={1}, email={2}, isTeacher={4}")
    @CsvSource({
            "홍길동, 01011111111, test1@a.b, !myPassword486@, true",
            "홍길순, 01022222222, test2@a.b, !myPassword486@, false"})
    void signInPasswordFail(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        registerRole();

        String signUpRequest = objectMapper.writeValueAsString(req);

        ReqSignIn reqSignIn = new ReqSignIn();
        ReflectionTestUtils.setField(reqSignIn, "email", req.getEmail());
        ReflectionTestUtils.setField(reqSignIn, "password", "wrongPassword");
        String content = objectMapper.writeValueAsString(reqSignIn);

        // when
        ResultActions signUp = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest));

        ResultActions perform = mockMvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        signUp
                .andExpect(status().isCreated())
                .andDo(print());

        perform
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    static class SignUpRequestAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            ReqSignUp reqSignUp = new ReqSignUp();
            ReflectionTestUtils.setField(reqSignUp, "name", accessor.getString(0));
            ReflectionTestUtils.setField(reqSignUp, "phoneNumber", accessor.getString(1));
            ReflectionTestUtils.setField(reqSignUp, "email", accessor.getString(2));
            ReflectionTestUtils.setField(reqSignUp, "password", accessor.getString(3));
            ReflectionTestUtils.setField(reqSignUp, "isTeacher", accessor.getString(4));
            return reqSignUp;
        }

    }


    private void registerRole() {
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_USER')").executeUpdate();
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_TEACHER')").executeUpdate();
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN')").executeUpdate();
    }

}