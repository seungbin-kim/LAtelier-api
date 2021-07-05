package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.Authority;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.enumeration.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;


    @BeforeEach
    void init() {
        Authority admin = new Authority(Role.ROLE_ADMIN);
        Authority user = new Authority(Role.ROLE_USER);
        em.persist(admin);
        em.persist(user);

        Member member = Member.builder()
                .name("홍길동")
                .phoneNumber("01012341234")
                .password("test")
                .email("test1@test.com")
                .build();
        member.getAuthorities().add(admin);
        member.getAuthorities().add(user);
        em.persist(member);
    }


    @Test
    @DisplayName("성공_휴대폰번호_존재유무")
    void existsByPhoneNumberSuccess() {
        // given
        String phoneNumber = "01012341234";

        // when
        boolean existsByPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

        // then
        assertTrue(existsByPhoneNumber);
    }


    @Test
    @DisplayName("실패_휴대폰번호_존재유무")
    void existsByPhoneNumberFailure() {
        // given
        String phoneNumber = "01000000000";

        // when
        boolean existsByPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

        // then
        assertFalse(existsByPhoneNumber);
    }


    @Test
    @DisplayName("성공_이메일_존재유무")
    void existsByEmailSuccess() {
        // given
        String email = "test1@test.com";

        // when
        boolean existsByEmail = memberRepository.existsByEmail(email);

        // then
        assertTrue(existsByEmail);
    }


    @Test
    @DisplayName("성공_이메일_존재유무")
    void existsByEmailFailure() {
        // given
        String email = "test99@test.com";

        // when
        boolean existsByEmail = memberRepository.existsByEmail(email);

        // then
        assertFalse(existsByEmail);
    }


    @Test
    @DisplayName("성공_권한과_함께_유저조회")
    void findMemberByEmailWithAuthoritiesSuccess() {
        // given
        String email = "test1@test.com";

        // when
        Optional<Member> optionalMember = memberRepository.findByEmailWithAuthorities(email);

        // then
        assertTrue(optionalMember.isPresent());
        assertEquals(2, optionalMember.get().getAuthorities().size());
    }

}