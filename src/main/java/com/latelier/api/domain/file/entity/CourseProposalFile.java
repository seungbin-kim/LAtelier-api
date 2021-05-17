package com.latelier.api.domain.file.entity;

import com.latelier.api.domain.course.entity.CourseProposal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "COURSE_PROPOSAL_FILE_SEQ_GENERATOR",
    sequenceName = "COURSE_PROPOSAL_FILE_SEQ",
    allocationSize = 10)
public class CourseProposalFile {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COURSE_PROPOSAL_FILE_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_proposal_id", columnDefinition = "bigint")
  private CourseProposal courseProposal;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id", columnDefinition = "bigint")
  private File file;

}
