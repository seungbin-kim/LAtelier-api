package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.packet.ReqSmsAuthentication;
import com.latelier.api.domain.member.service.SmsService;
import com.latelier.api.domain.member.service.ZoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final ZoomService zoomService;

  private final SmsService smsService;

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
  public ResponseEntity<?> callback(@RequestParam final String code,
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


  @PostMapping("/sms")
  @ApiOperation(
      value = "SMS 인증 문자보내기",
      notes = "인증번호를 생성하여 사용자에게 문자를 전송합니다.")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "request", value = "authorization code", required = true)})
  @ApiResponses({
      @ApiResponse(responseCode = "202", description = "인증번호 전송 성공")})
  public ResponseEntity<?> sendSms(@RequestBody @Valid final ReqSmsAuthentication request) {

    smsService.sendCertificationNumber(request.getPhoneNumber());
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

}
