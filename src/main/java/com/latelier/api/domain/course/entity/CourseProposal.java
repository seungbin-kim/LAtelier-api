package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "COURSE_PROPOSAL_SEQ_GENERATOR",
    sequenceName = "COURSE_PROPOSAL_SEQ",
    allocationSize = 1)
public class CourseProposal extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COURSE_PROPOSAL_SEQ_GENERATOR")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id", columnDefinition = "bigint")
  private User manager;

  @Column(length = 1000)
  private String explanation;

}
