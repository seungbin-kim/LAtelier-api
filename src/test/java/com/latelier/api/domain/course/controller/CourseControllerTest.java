package com.latelier.api.domain.course.controller;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.MeetingInformation;
import com.latelier.api.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;


    @Test
    @WithMockUser(value = "1", roles = {"USER"})
    @DisplayName("강의_입장정보_요청성공")
    void getMeeting() throws Exception {
        // given
        String name = "홍길동";
        String email = "test@a.b";
        String phoneNumber = "01074787389";
        String password = "pwd";

        registerRole();
        Member member = Member.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
        em.persist(member);

        String courseName = "테스트";
        Course course = Course.builder()
                .teacher(member)
                .courseName(courseName)
                .build();
        em.persist(course);
        Long courseId = course.getId();

        String meetingId = "000000";
        String meetingPw = "pwd";
        MeetingInformation meetingInformation = new MeetingInformation(
                course,
                meetingId,
                meetingPw);
        em.persist(meetingInformation);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/courses/{courseId}/participation-information", courseId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.apiKey").exists())
                .andExpect(jsonPath("$.content.meetingNumber").exists())
                .andExpect(jsonPath("$.content.meetingPassword").exists())
                .andExpect(jsonPath("$.content.userName").exists())
                .andExpect(jsonPath("$.content.signature").exists())
                .andDo(print());
    }


    private void registerRole() {
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_USER')").executeUpdate();
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_TEACHER')").executeUpdate();
        em.createNativeQuery("INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN')").executeUpdate();
    }

}