package com.latelier.api.domain.course.controller;

import com.latelier.api.domain.course.packet.response.ResMeetingInformation;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.model.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseController {

    private final MeetingInformationService meetingInformationService;


    @GetMapping("/courses/{courseId}/participation-information")
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(
            value = "강의 입장정보 요청",
            notes = "SDK 에서 강의에 입장하기위한 정보를 요청합니다.",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataType = "long", paramType = "path", example = "1")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입장정보 반환 성공")})
    public ResponseEntity<Result<ResMeetingInformation>> getMeeting(@PathVariable final Long courseId) {

        return ResponseEntity.ok(Result.of(meetingInformationService.getMeetingInformation(courseId)));
    }

}
