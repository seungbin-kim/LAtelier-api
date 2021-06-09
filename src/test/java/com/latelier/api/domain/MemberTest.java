package com.latelier.api.domain;

import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@AutoConfigureTestDatabase
@DataJpaTest
class MemberTest {

  private final Logger log = LoggerFactory.getLogger(MemberTest.class);

  @Autowired
  MemberRepository memberRepository;


  @Test
  @DisplayName("생성일_수정일_테스트")
  void jpaAuditingTest() throws Exception {
    //given
    LocalDateTime now = LocalDateTime.now();
    Member member = Member.builder()
        .email("test@test.com")
        .phoneNumber("01012345678")
        .name("test")
        .nickname("testNickname")
        .build();

    //when
    Member savedMember = memberRepository.save(member);

    //then
    log.info("createdAt: {}, modifiedAt: {}", savedMember.getCreatedAt(), savedMember.getUpdatedAt());
    Assertions.assertThat(savedMember.getCreatedAt()).isAfter(now);
    Assertions.assertThat(savedMember.getUpdatedAt()).isAfter(now);
  }

}