package com.latelier.api.domain.course.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "MEETING_INFORMATION_SEQ_GENERATOR",
    sequenceName = "MEETING_INFORMATION_SEQ",
    allocationSize = 1)
public class MeetingInformation {

  @Builder
  private MeetingInformation(Course course, String meetingId, String meetingPw) {
    this.course = course;
    this.meetingId = meetingId;
    this.meetingPw = meetingPw;
  }

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

}
