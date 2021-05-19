package com.latelier.api.domain.user.entity;

import com.latelier.api.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "ENROLMENT_SEQ_GENERATOR",
    sequenceName = "ENROLMENT_SEQ",
    allocationSize = 1)
public class Enrolment {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "ENROLMENT_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", columnDefinition = "bigint")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", columnDefinition = "bigint")
  private Course course;

}
