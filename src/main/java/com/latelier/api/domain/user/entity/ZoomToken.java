package com.latelier.api.domain.user.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "ZOOM_TOKEN_SEQ_GENERATOR",
    sequenceName = "ZOOM_TOKEN_SEQ",
    allocationSize = 10)
public class ZoomToken {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "ZOOM_TOKEN_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @Column(length = 1000)
  private String accessToken;

}
