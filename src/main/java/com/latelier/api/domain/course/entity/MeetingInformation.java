package com.latelier.api.domain.course.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "MEETING_INFORMATION_SEQ_GENERATOR",
        sequenceName = "MEETING_INFORMATION_SEQ")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "course_id_unique", columnNames = {"course_id"}),
                @UniqueConstraint(name = "course_id_unique", columnNames = {"meetingId"})})
public class MeetingInformation {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEETING_INFORMATION_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", columnDefinition = "bigint")
    private Course course;

    @Column(length = 50)
    private String meetingId;

    @Column(length = 50)
    private String meetingPw;

    @Column(length = 100)
    private String joinUrl;


    private MeetingInformation(final Course course,
                               final String meetingId,
                               final String meetingPw,
                               final String joinUrl) {

        this.course = course;
        this.meetingId = meetingId;
        this.meetingPw = meetingPw;
        this.joinUrl = joinUrl;
    }


    public static MeetingInformation of(final Course course,
                                        final String meetingId,
                                        final String meetingPw,
                                        final String joinUrl) {

        return new MeetingInformation(course, meetingId, meetingPw, joinUrl);
    }

}
