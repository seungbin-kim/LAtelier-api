package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.service.ZoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final ZoomService zoomService;

  /*
  TODO ControllerAdvise 작성하기. URISyntaxException, RestClientException, exception 클래스의 예외들
   */

  @GetMapping("/zoom/callback")
  @ApiOperation(
      value = "Zoom OAuth 인증과 회의생성 API 호출",
      notes = "Zoom OAuth 인증 후 회의생성 API 를 호출하여 start url 을 반환합니다.")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "code", value = "authorization code", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "state", value = "회의를 생성할 강의 ID", required = true, dataType = "long", paramType = "query")
  })
  public ResponseEntity<?> zoomCallback(@RequestParam final String code,
                                        @RequestParam(name = "state") final Long courseId) throws URISyntaxException {

    /*
    TODO 이미 회의가 생성된경우(미팅정보존재시), 또 생성하지 못하게 해야함(시작 버튼을 2번누를시 문제)
          미팅정보가 들어가는 테이블 유니크조건걸기(동일강의 2개가있으면 안됨)
          강의종료시 테이블내용 지워야함
     */

    String startUrl = zoomService.createCourseMeeting(code, courseId);

    URI uri = new URI(startUrl);
    return ResponseEntity.status(HttpStatus.SEE_OTHER).location(uri).build();
  }

}
