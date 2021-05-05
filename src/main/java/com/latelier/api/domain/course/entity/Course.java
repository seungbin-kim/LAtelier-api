package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
    name = "COURSE_SEQ_GENERATOR",
    sequenceName = "COURSE_SEQ",
    allocationSize = 10)
public class Course extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COURSE_SEQ_GENERATOR")
  @Column(name = "course_id", columnDefinition = "bigint")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id", columnDefinition = "bigint")
  private User teacher;

  @Column(length = 50)
  private String className;

  @Column(length = 1000)
  private String explanation;

  @Column(columnDefinition = "int")
  private Integer price;

  @Column(columnDefinition = "int")
  private Integer currentSize;

  @Column(columnDefinition = "int")
  private Integer maxSize;

  private LocalDateTime endDate;

}
