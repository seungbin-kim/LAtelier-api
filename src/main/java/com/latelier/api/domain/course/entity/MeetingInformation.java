package com.latelier.api.domain.course.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "MEETING_INFORMATION_SEQ_GENERATOR",
    sequenceName = "MEETING_INFORMATION_SEQ",
    allocationSize = 10)
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

}
