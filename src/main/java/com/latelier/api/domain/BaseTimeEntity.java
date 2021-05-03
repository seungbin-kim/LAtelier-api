package com.latelier.api.domain;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

}
