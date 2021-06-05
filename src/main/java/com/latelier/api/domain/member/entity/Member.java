package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.file.entity.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "MEMBER_SEQ_GENERATOR",
    sequenceName = "MEMBER_SEQ",
    allocationSize = 1)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "email_unique", columnNames = {"email"}),
        @UniqueConstraint(name = "phone_number_unique", columnNames = {"phoneNumber"})})
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "MEMBER_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @Column(length = 50, nullable = false)
  private String email;

  @Column(length = 20, nullable = false)
  private String phoneNumber;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "icon_file_id", columnDefinition = "bigint")
  private File file;

  @Column(length = 20)
  private String name;

  @Column(length = 20)
  private String nickname;

  @Column(length = 100)
  private String password;

  @Column(length = 500)
  private String introduction;

  @Embedded
  private Address address;

  private boolean isActive;


  @Builder
  public Member(String email, String phoneNumber) {
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

}
