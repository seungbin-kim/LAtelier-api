package com.latelier.api.domain;

import com.latelier.api.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@AutoConfigureTestDatabase
@DataJpaTest
class UserTest {

  private final Logger log = LoggerFactory.getLogger(UserTest.class);

  @Autowired
  EntityManager em;

  @Test
  @DisplayName("생성일_수정일_테스트")
  void jpaAuditingTest() throws Exception {
    //given
    LocalDateTime now = LocalDateTime.now();
    User user = User.builder()
        .email("test@test.com")
        .phoneNumber("01012345678")
        .build();

    //when
    em.persist(user);

    //then
    log.info("createdAt: {}, modifiedAt: {}", user.getCreatedAt(), user.getModifiedAt());
    Assertions.assertThat(user.getCreatedAt()).isAfter(now);
    Assertions.assertThat(user.getModifiedAt()).isAfter(now);
  }

}