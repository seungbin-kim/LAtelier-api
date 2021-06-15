package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.member.enumeration.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

  @Id
  @Column(name = "authority_name", length = 20)
  @Enumerated(EnumType.STRING)
  private Role authorityName;


  public Authority(final Role authorityName) {
    this.authorityName = authorityName;
  }

}
