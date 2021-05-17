package com.latelier.api.domain.course.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "CATEGORY_SEQ_GENERATOR",
    sequenceName = "CATEGORY_SEQ",
    allocationSize = 10)
public class Category {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "CATEGORY_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @Column(length = 10)
  private String categoryName;

}
