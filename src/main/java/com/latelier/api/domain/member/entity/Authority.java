package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.member.enumeration.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

  @Id
  @Column(length = 20)
  @Enumerated(EnumType.STRING)
  private Role authorityName;

}
