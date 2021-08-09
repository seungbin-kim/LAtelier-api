package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.member.enumeration.Role;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ")
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
    private String username;

    @Column(length = 20)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 500)
    private String introduction;

    @Embedded
    private Address address;

    private boolean activated = true;

    @Column(name = "authority", length = 20)
    @Enumerated(EnumType.STRING)
    private Role authority;


    private Member(final String email,
                   final String phoneNumber,
                   final String username,
                   final String nickname,
                   final String password) {

        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }


    /**
     * Member 생성 메서드
     *
     * @param email       이메일
     * @param phoneNumber 휴대폰번호
     * @param username    실명
     * @param password    비밀번호
     * @param role        가입 구분
     * @return 생성된 Member Entity
     */
    public static Member of(final String email,
                            final String phoneNumber,
                            final String username,
                            final String password,
                            final String role) {

        Member member = new Member(email, phoneNumber, username, username, password);
        setAuthority(member, role);
        return member;
    }


    private static void setAuthority(final Member member, final String role) {
        switch (role) {
            case "admin":
                member.authority = Role.ROLE_ADMIN;
                break;
            case "instructor":
                member.authority = Role.ROLE_INSTRUCTOR;
                break;
            case "user":
                member.authority = Role.ROLE_USER;
                break;
        }
    }


    public void changeActiveState(boolean bool) {
        this.activated = bool;
    }

}
