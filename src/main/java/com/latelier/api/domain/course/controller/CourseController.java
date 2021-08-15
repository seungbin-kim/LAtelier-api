package com.latelier.api.domain.course.controller;

import com.latelier.api.domain.course.packet.request.ReqCourseRegister;
import com.latelier.api.domain.course.packet.response.ResCourseRegister;
import com.latelier.api.domain.course.packet.response.ResMeetingInformation;
import com.latelier.api.domain.course.service.CourseService;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.model.Result;
import com.latelier.api.domain.util.SecurityUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final MeetingInformationService meetingInformationService;

    private final CourseService courseService;

    private final SecurityUtil securityUtil;


    @GetMapping("/{courseId}/participation-information")
    @ApiOperation(
            value = "강의 입장정보 요청",
            notes = "SDK 에서 강의에 입장하기위한 정보를 요청합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입장정보 반환 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 입장정보를 찾을 수 없음")})
    public ResponseEntity<Result<ResMeetingInformation>> getMeeting(@PathVariable final Long courseId) {

        return ResponseEntity.ok(Result.of(meetingInformationService.getMeetingInformation(courseId)));
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping(consumes = "multipart/form-data")
    @ApiOperation(
            value = "강의 등록(신청)",
            notes = "강의를 등록(신청)합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강의등록(신청) 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "파일 입출력 실패 등 내부 서버에러")})
    public ResponseEntity<Result<ResCourseRegister>> registerCourse(@Valid final ReqCourseRegister reqCourseRegister) {

        ResCourseRegister response = courseService.addCourse(securityUtil.getMemberId(), reqCourseRegister);
        return ResponseEntity.ok(Result.of(response));
    }

}
