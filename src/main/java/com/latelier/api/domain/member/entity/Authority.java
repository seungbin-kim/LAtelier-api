package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.member.enumeration.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "AUTHORITY_SEQ_GENERATOR",
    sequenceName = "AUTHORITY_SEQ",
    allocationSize = 1)
public class Authority {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "AUTHORITY_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @Column(length = 20)
  @Enumerated(EnumType.STRING)
  private Role authorityName;

}
