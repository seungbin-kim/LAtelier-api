package com.latelier.api.controller;

import com.latelier.api.domain.course.service.CourseService;
import com.latelier.api.domain.member.packet.response.ResAddCart;
import com.latelier.api.domain.member.packet.response.ResMyCart;
import com.latelier.api.domain.member.packet.response.ResMyCourse;
import com.latelier.api.domain.member.service.MemberService;
import com.latelier.api.domain.model.Result;
import com.latelier.api.domain.util.SecurityUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final CourseService courseService;

    private final SecurityUtil securityUtil;


    @PostMapping("/me/cart/{courseId}")
    @ApiOperation(
            value = "장바구니 추가",
            notes = "장바구니에 강의를 추가합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 추가 성공"),
            @ApiResponse(responseCode = "400", description = "이미 구매한 강의"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않아 추가불가"),
            @ApiResponse(responseCode = "404", description = "강의 또는 현재 유저를 찾지 못함"),
            @ApiResponse(responseCode = "409", description = "이미 장바구니에 있음")})
    public ResponseEntity<Result<ResAddCart>> addMyCart(@PathVariable final Long courseId) {

        ResAddCart response = memberService.addInUserCart(securityUtil.getMemberId(), courseId);
        return ResponseEntity.status(CREATED)
                .body(Result.of(response));
    }


    @GetMapping("/me/cart")
    @ApiOperation(
            value = "장바구니 목록 조회",
            notes = "유저의 장바구니에 목록을 조회합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 추가 성공"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않아 조회불가"),
            @ApiResponse(responseCode = "404", description = "현재 유저를 찾지 못함")})
    public ResponseEntity<Result<ResMyCart>> getMyCart() {

        ResMyCart response = memberService.getUserCartList(securityUtil.getMemberId());
        return ResponseEntity.ok(Result.of(response));
    }

    
    @DeleteMapping("/me/cart/{cartId}")
    @ApiOperation(
            value = "장바구니 강의 제거",
            notes = "장바구니에 있는 특정 요소를 제거합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cartId", value = "장바구니 요소 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "제거 성공"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않음"),
            @ApiResponse(responseCode = "404", description = "장바구니에 있는 대상 또는 유저를 찾지 못함")})
    public ResponseEntity<Void> deleteMyCart(@PathVariable final Long cartId) {

        memberService.deleteInUserCart(securityUtil.getMemberId(), cartId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    
    @DeleteMapping("/me/cart")
    @ApiOperation(
            value = "장바구니 강의 제거",
            notes = "장바구니에 있는 특정 강의를 제거합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "제거 성공"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않음"),
            @ApiResponse(responseCode = "404", description = "유저를 찾지 못함")})
    public ResponseEntity<Void> deleteAllMyCart() {

        memberService.deleteAllInUserCart(securityUtil.getMemberId());
        return ResponseEntity.status(NO_CONTENT).build();
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/me/teaching-courses")
    @ApiOperation(
            value = "강사 강의목록 얻기",
            protocols = "강사의 강의목록들을 조회합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "강사가 아님"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")})
    public ResponseEntity<Page<ResMyCourse>> getTeachingCourses(final Pageable pageable) {

        Page<ResMyCourse> response = courseService.getMyCourses(securityUtil.getMemberId(), pageable, true);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/me/courses")
    @ApiOperation(
            value = "나의 강의목록 얻기",
            protocols = "수강하는 강의목록들을 조회합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인중이 아님"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")})
    public ResponseEntity<Page<ResMyCourse>> getCourses(final Pageable pageable) {

        Page<ResMyCourse> response = courseService.getMyCourses(securityUtil.getMemberId(), pageable, false);
        return ResponseEntity.ok(response);
    }

}