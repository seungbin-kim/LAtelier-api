package com.latelier.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "MEMBER_AUTHORITY_SEQ_GENERATOR",
    sequenceName = "USER_AUTHORITY_SEQ",
    allocationSize = 1)
public class MemberAuthority {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "MEMBER_AUTHORITY_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id", columnDefinition = "bigint")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "authority_id", columnDefinition = "bigint")
  private Authority authority;

}
