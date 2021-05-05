package com.latelier.api.domain.course.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(
    name = "COURSE_BOARD_FILE_SEQ_GENERATOR",
    sequenceName = ""
)
public class CourseBoardFile {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "COURSE_BOARD_FILE_SEQ_GENERATOR")
  private Long id;

  

}
