package com.latelier.api.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultResponse<T> {

  private final T content;

}
