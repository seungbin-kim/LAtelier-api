package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "COURSE_REVIEW_SEQ_GENERATOR",
    sequenceName =  "COURSE_REVIEW_SEQ",
    allocationSize = 10)
public class CourseReview {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COURSE_REVIEW_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", columnDefinition = "bigint")
  private Course course;

  @Column(columnDefinition = "text")
  private String content;

  @Column(columnDefinition = "smallint")
  private short rating;

}
