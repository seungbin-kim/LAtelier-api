package com.latelier.api.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.latelier.api.domain.course.service.ZoomService;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.packet.request.ReqSignIn;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.request.ReqSmsAuthentication;
import com.latelier.api.domain.member.packet.request.ReqSmsVerification;
import com.latelier.api.domain.member.packet.response.ResSignIn;
import com.latelier.api.domain.member.packet.response.ResSignUp;
import com.latelier.api.domain.member.service.MemberService;
import com.latelier.api.domain.member.service.SmsService;
import com.latelier.api.domain.model.Result;
import com.latelier.api.domain.util.SecurityUtil;
import com.latelier.api.domain.util.TokenProvider;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ZoomService zoomService;

    private final SmsService smsService;

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    private final SecurityUtil securityUtil;


    @PreAuthorize("isAnonymous()")
    @PostMapping("/members")
    @ApiOperation(
            value = "사용자 등록",
            notes = "사용자 정보를 등록하면서 쿠키에 JWT 를 설정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 이상"),
            @ApiResponse(responseCode = "403", description = "이미 로그인 상태"),
            @ApiResponse(responseCode = "409", description = "이메일 또는 휴대폰번호 중복")})
    public ResponseEntity<Result<ResSignUp>> signUp(@RequestBody @Valid final ReqSignUp reqSignUp) {

        Member member = memberService.addMember(reqSignUp);
        String token = tokenProvider.createToken(member);
        ResponseCookie tokenCookie = tokenProvider.createTokenCookie(token);

        ResSignUp response = ResSignUp.of(member);
        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body(Result.of(response));
    }


    @PreAuthorize("isAnonymous()")
    @PostMapping("/sign-in")
    @ApiOperation(
            value = "로그인",
            notes = "로그인 정보를 받아 쿠키에 JWT 를 설정하고, 유저의 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인처리 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 또는 비밀번호 불일치"),
            @ApiResponse(responseCode = "403", description = "이미 로그인 되어있는 경우")})
    public ResponseEntity<Result<ResSignIn>> signIn(@RequestBody @Valid final ReqSignIn reqSignIn) {

        Member member = memberService.authenticate(reqSignIn.getEmail(), reqSignIn.getPassword());
        String token = tokenProvider.createToken(member);
        ResponseCookie tokenCookie = tokenProvider.createTokenCookie(token);

        ResSignIn response = ResSignIn.of(member);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body(Result.of(response));
    }


    @PostMapping("/sign-out")
    @ApiOperation(
            value = "로그아웃",
            notes = "쿠키의 JWT 를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "이미 로그아웃 되어있는 경우")})
    public ResponseEntity<Void> signOut() {

        ResponseCookie responseCookie = tokenProvider.createTokenCookie("");
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }


    @GetMapping("/check")
    @ApiOperation(
            value = "로그인상태 체크",
            notes = "쿠키의 JWT 를 확인하여 로그인 상태를 확인합니다. 토큰의 만료기한이 짧아지면 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 상태"),
            @ApiResponse(responseCode = "401", description = "로그인 중이 아님")})
    public ResponseEntity<Result<ResSignIn>> check(@ApiIgnore @CookieValue(required = false) final String token) {

        Member member = memberService.getMemberById(securityUtil.getMemberId());

        DecodedJWT decodedJWT = tokenProvider.validateAndDecodeToken(token);
        if (tokenProvider.checkTokenReissue(decodedJWT)) {
            String ReissuedToken = tokenProvider.createToken(securityUtil.getAuthentication());
            ResponseCookie responseCookie = tokenProvider.createTokenCookie(ReissuedToken);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(Result.of(ResSignIn.of(member)));
        }
        return ResponseEntity.ok()
                .body(Result.of(ResSignIn.of(member)));
    }


    @PostMapping("/sms")
    @ApiOperation(
            value = "SMS 인증 문자보내기",
            notes = "인증번호를 생성하여 사용자에게 문자를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "인증번호 전송 성공"),
            @ApiResponse(responseCode = "409", description = "휴대폰 번호 중복"),
            @ApiResponse(responseCode = "500", description = "메세지 전송 실패")})
    public ResponseEntity<Void> sendSms(@RequestBody @Valid final ReqSmsAuthentication request) {

        smsService.sendCertificationNumber(request.getPhoneNumber());
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/sms/verification")
    @ApiOperation(
            value = "SMS 인증번호 확인",
            notes = "전송된 인증번호의 일치여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 확인 성공"),
            @ApiResponse(responseCode = "400", description = "인증번호 확인 실패")})
    public ResponseEntity<Void> checkSMS(@RequestBody @Valid final ReqSmsVerification request) {

        smsService.verifySMS(request.getPhoneNumber(), request.getCertificationNumber());
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
