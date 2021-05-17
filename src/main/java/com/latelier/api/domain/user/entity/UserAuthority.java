package com.latelier.api.domain.user.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "USER_AUTHORITY_SEQ_GENERATOR",
    sequenceName = "USER_AUTHORITY_SEQ",
    allocationSize = 10)
public class UserAuthority {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "USER_AUTHORITY_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @ManyToOne
  @JoinColumn(name = "authority_id", columnDefinition = "bigint")
  private Authority authority;

}
