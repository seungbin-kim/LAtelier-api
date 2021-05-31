package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  boolean existsByPhoneNumber(final String phoneNumber);

}
