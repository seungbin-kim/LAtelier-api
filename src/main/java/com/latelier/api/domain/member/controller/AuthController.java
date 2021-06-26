package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.packet.request.ReqSignIn;
import com.latelier.api.domain.member.packet.request.ReqSmsAuthentication;
import com.latelier.api.domain.member.packet.request.ReqSmsVerification;
import com.latelier.api.domain.member.packet.response.ResToken;
import com.latelier.api.domain.member.service.MemberAuthService;
import com.latelier.api.domain.member.service.SmsService;
import com.latelier.api.domain.member.service.ZoomService;
import com.latelier.api.domain.model.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ZoomService zoomService;

    private final SmsService smsService;

    private final MemberAuthService signInService;


    @PostMapping("/sign-in")
    @ApiOperation(
            value = "로그인",
            notes = "로그인 정보를 받아 Json Web Token 을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인처리 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 또는 비밀번호 불일치")})
    public ResponseEntity<Result<ResToken>> signIn(@RequestBody @Valid final ReqSignIn reqSignIn) {

        String token = signInService.signIn(reqSignIn);
        ResToken resToken = new ResToken(token);
        return ResponseEntity
                .ok(Result.<ResToken>builder()
                        .content(resToken)
                        .build());
    }


    @GetMapping("/zoom/callback")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @ApiOperation(
            value = "Zoom OAuth 인증과 회의생성 API 호출",
            notes = "Zoom OAuth 인증 후 회의생성 API 를 호출하여 start url 을 반환합니다.",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "authorization code", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "state", value = "회의를 생성할 강의 ID", required = true, dataType = "long", paramType = "query")})
    @ApiResponses({
            @ApiResponse(responseCode = "303", description = "성공적으로 회의가 생성되어 start url 반환"),
            @ApiResponse(responseCode = "500", description = "액세스 토큰을 얻지 못하거나 회의 생성에 실패")})
    public ResponseEntity<Void> callback(@RequestParam final String code,
                                         @RequestParam(name = "state") final Long courseId) throws URISyntaxException {

    /*
    TODO 이미 회의가 생성된경우(미팅정보존재시), 또 생성하지 못하게 해야함(시작 버튼을 2번누를시 문제)
          미팅정보가 들어가는 테이블 유니크조건걸기(동일강의 2개가있으면 안됨)
          강의종료시 테이블내용은 지워져야함...!
     */

        String startUrl = zoomService.createCourseMeeting(code, courseId);

        URI uri = new URI(startUrl);
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .location(uri)
                .build();
    }


    @PostMapping("/sms")
    @ApiOperation(
            value = "SMS 인증 문자보내기",
            notes = "인증번호를 생성하여 사용자에게 문자를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "인증번호 전송 성공"),
            @ApiResponse(responseCode = "409", description = "휴대폰 번호 중복"),
            @ApiResponse(responseCode = "500", description = "메세지 전송 실패")})
    public ResponseEntity<Void> sendSms(@RequestBody @Valid final ReqSmsAuthentication request) {

        smsService.sendCertificationNumber(request.getPhoneNumber());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }


    @PostMapping("/sms/verification")
    @ApiOperation(
            value = "SMS 인증번호 확인",
            notes = "전송된 인증번호의 일치여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 확인 성공"),
            @ApiResponse(responseCode = "400", description = "인증번호 확인 실패")})
    public ResponseEntity<Void> checkSMS(@RequestBody @Valid ReqSmsVerification request) {

        smsService.verifySMS(request.getPhoneNumber(), request.getCertificationNumber());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
