package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "COMMENT_SEQ_GENERATOR",
    sequenceName = "COMMENT_SEQ",
    allocationSize = 1)
public class Comment {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COMMENT_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", columnDefinition = "bigint")
  private CourseBoard board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @Column(length = 500)
  private String content;

}
