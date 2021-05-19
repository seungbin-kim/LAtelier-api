package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.user.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "COURSE_BOARD_SEQ_GENERATOR",
    sequenceName = "COURSE_BOARD_SEQ",
    allocationSize = 1)
public class CourseBoard extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COURSE_BOARD_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", columnDefinition = "bigint")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", columnDefinition = "bigint")
  private Member member;

  @Column(length = 50)
  private String title;

  @Column(columnDefinition = "text")
  private String content;

}
