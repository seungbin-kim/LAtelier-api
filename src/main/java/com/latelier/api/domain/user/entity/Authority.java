package com.latelier.api.domain.user.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "AUTHORITY_SEQ_GENERATOR",
    sequenceName = "AUTHORITY_SEQ",
    allocationSize = 10)
public class Authority {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "AUTHORITY_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @Column(length = 10)
  private String authorityName;

}
