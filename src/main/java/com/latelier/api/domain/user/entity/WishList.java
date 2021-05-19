package com.latelier.api.domain.user.entity;

import com.latelier.api.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "WISH_LIST_SEQ_GENERATOR",
    sequenceName = "WISH_LIST_SEQ",
    allocationSize = 1)
public class WishList {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "WISH_LIST_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", columnDefinition = "bigint")
  private Course course;

}
