package com.latelier.api.domain.course.controller;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.MeetingInformation;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.enumeration.Role;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @WithMockUser(value = "1")
    @DisplayName("강의_입장정보_요청성공")
    void getMeeting() throws Exception {
        // given
        String username = "홍길동";
        String email = "test@a.b";
        String phoneNumber = "01074787389";
        String password = "pwd";

        Member member = Member.of(email, phoneNumber, username, password, Role.ROLE_INSTRUCTOR.getRoleName());
        em.persist(member);

        String courseName = "테스트";
        Course course = Course.of(member, courseName, "", null, null, null, null);
        em.persist(course);
        Long courseId = course.getId();

        String meetingId = "000000";
        String meetingPw = "pwd";
        MeetingInformation meetingInformation = MeetingInformation.of(course, meetingId, meetingPw);
        em.persist(meetingInformation);

        // when
        ResultActions perform = mockMvc.perform(get("/api/courses/{courseId}/participation-information", courseId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.apiKey").exists())
                .andExpect(jsonPath("$.content.meetingNumber").exists())
                .andExpect(jsonPath("$.content.meetingPassword").exists())
                .andExpect(jsonPath("$.content.username").exists())
                .andExpect(jsonPath("$.content.signature").exists())
                .andDo(print());
    }


    @Test
    @DisplayName("강의_입장정보_요청실패_비회원")
    void getMeetingNonMemberFail() throws Exception {
        // given
        String username = "홍길동";
        String email = "test@a.b";
        String phoneNumber = "01074787389";
        String password = "pwd";

        Member member = Member.of(email, phoneNumber, username, password, Role.ROLE_INSTRUCTOR.getRoleName());
        em.persist(member);

        String courseName = "테스트";
        Course course = Course.of(member, courseName, "", null, null, null, null);
        em.persist(course);
        Long courseId = course.getId();

        String meetingId = "000000";
        String meetingPw = "pwd";
        MeetingInformation meetingInformation = MeetingInformation.of(course, meetingId, meetingPw);
        em.persist(meetingInformation);

        // when
        ResultActions perform = mockMvc.perform(get("/api/courses/{courseId}/participation-information", courseId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "1")
    @DisplayName("강의_입장정보_요청실패_열려있지_않은_강의")
    void getMeetingNotOpenFail() throws Exception {
        // given
        String username = "홍길동";
        String email = "test@a.b";
        String phoneNumber = "01074787389";
        String password = "pwd";

        Member member = Member.of(email, phoneNumber, username, password, Role.ROLE_INSTRUCTOR.getRoleName());
        em.persist(member);

        String courseName = "테스트";
        Course course = Course.of(member, courseName, "", null, null, null, null);
        em.persist(course);

        String meetingId = "000000";
        String meetingPw = "pwd";
        MeetingInformation meetingInformation = MeetingInformation.of(course, meetingId, meetingPw);
        em.persist(meetingInformation);

        // when
        ResultActions perform = mockMvc.perform(get("/api/courses/{courseId}/participation-information", 100)
                .accept(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isNotFound())
                .andDo(print());
    }

}