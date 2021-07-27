package com.latelier.api.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ApiModel("응답")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

  @ApiModelProperty(
      value = "응답 데이터",
      name = "content")
  private final T content;


  public static <T> Result<T> of(final T content) {
    return new Result<>(content);
  }

}
