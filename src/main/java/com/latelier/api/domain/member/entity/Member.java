package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.file.entity.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
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

  @Column(length = 20, nullable = false)
  private String name;

  @Column(length = 20, nullable = false)
  private String nickname;

  @Column(length = 100, nullable = false)
  private String password;

  @Column(length = 500)
  private String introduction;

  @Embedded
  private Address address;

  private boolean isActive = true;


  @Builder
  private Member(final String email, final String phoneNumber, final File file,
                final String name, final String nickname, final String password,
                final String introduction, final Address address) {

    this.email = email;
    this.phoneNumber = phoneNumber;
    this.file = file;
    this.name = name;
    this.nickname = nickname;
    this.password = password;
    this.introduction = introduction;
    this.address = address;
  }


  public void changeActiveState(boolean bool) {
    this.isActive = bool;
  }

}
