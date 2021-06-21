package com.latelier.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latelier.api.domain.member.packet.request.ReqSignIn;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
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


    @Test
    @DisplayName("회원등록후_로그인_성공")
    void signIn() throws Exception {
        // given
        registerRole();

        String email = "test@a.b";
        String password = "!mypassword486@";
        String phoneNumber = "01000000000";
        String name = "홍길동";

        ReqSignUp reqSignUp = getReqSignUp(email, password, phoneNumber, name);
        String signUpRequest = objectMapper.writeValueAsString(reqSignUp);

        ReqSignIn reqSignIn = new ReqSignIn();
        ReflectionTestUtils.setField(reqSignIn, "email", email);
        ReflectionTestUtils.setField(reqSignIn, "password", password);
        String content = objectMapper.writeValueAsString(reqSignIn);

        // when
        ResultActions signUp = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest));

        ResultActions perform = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        signUp
                .andExpect(status().isOk())
                .andDo(print());

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.token").isNotEmpty())
                .andDo(print());
    }


    @Test
    @DisplayName("회원등록후_로그인_실패(이메일)")
    void signInFailEmail() throws Exception {
        // given
        registerRole();

        String email = "test@a.b";
        String wrongEmail = "test1@a.b";
        String password = "!mypassword486@";
        String phoneNumber = "01000000000";
        String name = "홍길동";

        ReqSignUp reqSignUp = getReqSignUp(email, password, phoneNumber, name);
        String signUpRequest = objectMapper.writeValueAsString(reqSignUp);

        ReqSignIn reqSignIn = new ReqSignIn();
        ReflectionTestUtils.setField(reqSignIn, "email", wrongEmail);
        ReflectionTestUtils.setField(reqSignIn, "password", password);
        String content = objectMapper.writeValueAsString(reqSignIn);

        // when
        ResultActions signUp = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest));

        ResultActions perform = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        signUp
                .andExpect(status().isOk())
                .andDo(print());

        perform
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("회원등록후_로그인_실패(비밀번호)")
    void signInPasswordFail() throws Exception {
        // given
        registerRole();

        String email = "test@a.b";
        String wrongPassword = "wrong";
        String password = "!mypassword486@";
        String phoneNumber = "01000000000";
        String name = "홍길동";

        ReqSignUp reqSignUp = getReqSignUp(email, password, phoneNumber, name);
        String signUpRequest = objectMapper.writeValueAsString(reqSignUp);

        ReqSignIn reqSignIn = new ReqSignIn();
        ReflectionTestUtils.setField(reqSignIn, "email", email);
        ReflectionTestUtils.setField(reqSignIn, "password", wrongPassword);
        String content = objectMapper.writeValueAsString(reqSignIn);

        // when
        ResultActions signUp = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest));

        ResultActions perform = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        signUp
                .andExpect(status().isOk())
                .andDo(print());

        perform
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    private ReqSignUp getReqSignUp(String email, String password, String phoneNumber, String name) {
        ReqSignUp reqSignUp = new ReqSignUp();
        ReflectionTestUtils.setField(reqSignUp, "name", name);
        ReflectionTestUtils.setField(reqSignUp, "email", email);
        ReflectionTestUtils.setField(reqSignUp, "phoneNumber", phoneNumber);
        ReflectionTestUtils.setField(reqSignUp, "password", password);
        return reqSignUp;
    }


    private void registerRole() {
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_USER')").executeUpdate();
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_TEACHER')").executeUpdate();
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN')").executeUpdate();
    }

}