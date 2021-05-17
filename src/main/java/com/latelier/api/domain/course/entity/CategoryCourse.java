package com.latelier.api.domain.course.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "CATEGORY_COURSE_SEQ_GENERATOR",
    sequenceName = "CATEGORY_COURSE_SEQ",
    allocationSize = 10)
public class CategoryCourse {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "CATEGORY_COURSE_SEQ_GENERATOR")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", columnDefinition = "bigint")
  private Category category;

  @ManyToOne
  @JoinColumn(name = "course_id", columnDefinition = "bigint")
  private Course course;

}
