package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  /**
   * 휴대폰번호 확인
   *
   * @param phoneNumber 사용자 휴대폰번호
   * @return 이미 있다면 true, 아니라면 false
   */
  boolean existsByPhoneNumber(String phoneNumber);


  /**
   * 이메일 확인
   *
   * @param email 사용자 이메일
   * @return 이미 있다면 true, 아니라면 false
   */
  boolean existsByEmail(String email);


  /**
   * 유저 email 로 조회하기
   *
   * @param email 조회할 유저 email
   * @return 조회된 Optional 유저
   */
  Optional<Member> findByEmail(String email);
}
