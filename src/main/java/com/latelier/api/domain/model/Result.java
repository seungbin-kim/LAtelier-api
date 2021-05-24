package com.latelier.api.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Result<T> {

  private final T content;

}
