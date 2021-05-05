package com.latelier.api.domain.file.entity;

import com.latelier.api.domain.model.BaseTimeEntity;

import javax.persistence.*;

@Entity
@SequenceGenerator(
    name = "FILE_SEQ_GENERATOR",
    sequenceName = "FILE_SEQ",
    allocationSize = 10)
public class File extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "FILE_SEQ_GENERATOR")
  @Column(name = "file_id", columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_group_id", columnDefinition = "bigint")
  private FileGroup fileGroup;

  @Column(length = 50)
  private String originalFileName;

  @Column(length = 50)
  private String storedFileName;

  @Column(columnDefinition = "bigint")
  private Long fileSize;

}
