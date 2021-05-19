package com.latelier.api.domain.user.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.file.entity.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "USER_SEQ_GENERATOR",
    sequenceName = "USER_SEQ",
    allocationSize = 1)
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "email_unique", columnNames = {"email"}),
        @UniqueConstraint(name = "phone_number_unique", columnNames = {"phoneNumber"})
    })
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "USER_SEQ_GENERATOR"
  )
  @Column(name = "user_id" ,columnDefinition = "bigint")
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

  @Column(length = 100)
  private String address;

  @Column(length = 10)
  private String zipcode;

  @Builder
  public User(String email, String phoneNumber) {
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

}
