package com.latelier.api.domain.file.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(
    name = "FILE_GROUP_SEQ_GENERATOR",
    sequenceName = "FILE_GROUP_SEQ",
    allocationSize = 10
)
public class FileGroup {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "FILE_GROUP_SEQ_GENERATOR")
  @Column(name = "file_group_id", columnDefinition = "bigint")
  private Long id;

  @Column(length = 10)
  private String file_group_name;

  @Column(length = 100)
  private String path;

}
