package com.latelier.api.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel("응답")
public class Result<T> {

  @ApiModelProperty(
      value = "응답 데이터",
      name = "content")
  private final T content;

}
