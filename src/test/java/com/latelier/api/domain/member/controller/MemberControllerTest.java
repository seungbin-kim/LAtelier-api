package com.latelier.api.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.enumeration.Role;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.repository.MemberRepository;
import com.latelier.api.global.error.exception.ErrorCode;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    ApplicationContext context;


    @DisplayName("회원등록_성공")
    @ParameterizedTest(name = "[{index}] username={0}, phoneNumber={1}, email={2}, role={4}")
    @CsvSource({
            "홍길동, 01011111111, test1@a.b, !myPassword486@, user",
            "홍길순, 01022222222, test2@a.b, !myPassword486@, instructor",
            "홍길복, 01033333333, test3@a.b, !myPassword486@, admin"})
    void signUpSuccess(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        String content = objectMapper.writeValueAsString(req);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.username").value(req.getUsername()))
                .andExpect(jsonPath("$.content.phoneNumber").value(req.getPhoneNumber()))
                .andExpect(jsonPath("$.content.email").value(req.getEmail()))
                .andExpect(jsonPath("$.content.role").value(req.getRole()))
                .andDo(print());
    }


    @DisplayName("회원등록_실패_이메일중복")
    @ParameterizedTest(name = "[{index}] email={2}")
    @CsvSource({
            "홍길동, 01011111111, test@a.b, !myPassword486@, user",
            "홍길순, 01022222222, test@a.b, !myPassword486@, instructor",
            "홍길복, 01033333333, test@a.b, !myPassword486@, admin"})
    void signUpEmailDuplicate(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        MemberRepository memberRepository = context.getBean(MemberRepository.class);
        Member member = Member.of(
                req.getEmail(),
                "01000000000",
                "테스터",
                "password",
                Role.ROLE_USER.getRoleName());
        memberRepository.save(member);

        String content = objectMapper.writeValueAsString(req);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ErrorCode.EMAIL_DUPLICATE.getMessage()))
                .andDo(print());
    }


    @DisplayName("회원등록_실패_휴대폰중복")
    @ParameterizedTest(name = "[{index}] phoneNumber={1}")
    @CsvSource({
            "홍길동, 01000000000, test1@a.b, !myPassword486@, user",
            "홍길순, 01000000000, test2@a.b, !myPassword486@, instructor",
            "홍길복, 01000000000, test3@a.b, !myPassword486@, admin"})
    void signUpPhoneNumberDuplicate(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        MemberRepository memberRepository = context.getBean(MemberRepository.class);
        Member member = Member.of(
                "test@a.b",
                req.getPhoneNumber(),
                "테스터",
                "password",
                Role.ROLE_USER.getRoleName());
        memberRepository.save(member);

        String content = objectMapper.writeValueAsString(req);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ErrorCode.PHONE_NUMBER_DUPLICATE.getMessage()))
                .andDo(print());
    }


    @DisplayName("회원등록_실패_모두중복")
    @ParameterizedTest(name = "[{index}] phoneNumber={1} email={2}")
    @CsvSource({
            "홍길동, 01000000000, test@a.b, !myPassword486@, user",
            "홍길순, 01000000000, test@a.b, !myPassword486@, instructor",
            "홍길복, 01000000000, test@a.b, !myPassword486@, admin"})
    void signUpDuplicate(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) throws Exception {
        // given
        MemberRepository memberRepository = context.getBean(MemberRepository.class);
        Member member = Member.of(
                req.getEmail(),
                req.getPhoneNumber(),
                "테스터",
                "password",
                Role.ROLE_USER.getRoleName());
        memberRepository.save(member);

        String content = objectMapper.writeValueAsString(req);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ErrorCode.EMAIL_AND_PHONE_NUMBER_DUPLICATE.getMessage()))
                .andDo(print());
    }


    static class SignUpRequestAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            ReqSignUp reqSignUp = new ReqSignUp();
            ReflectionTestUtils.setField(reqSignUp, "username", accessor.getString(0));
            ReflectionTestUtils.setField(reqSignUp, "phoneNumber", accessor.getString(1));
            ReflectionTestUtils.setField(reqSignUp, "email", accessor.getString(2));
            ReflectionTestUtils.setField(reqSignUp, "password", accessor.getString(3));
            ReflectionTestUtils.setField(reqSignUp, "role", accessor.getString(4));
            return reqSignUp;
        }

    }

}