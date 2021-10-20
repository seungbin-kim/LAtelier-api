package com.latelier.api.controller;

import com.latelier.api.domain.course.packet.request.ReqCourseRegister;
import com.latelier.api.domain.course.packet.response.ResCourseDetails;
import com.latelier.api.domain.course.packet.response.ResCourseRegister;
import com.latelier.api.domain.course.packet.response.ResCourseSimple;
import com.latelier.api.domain.course.packet.response.ResMeetingInformation;
import com.latelier.api.domain.course.service.CourseService;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.member.entity.Enrollment;
import com.latelier.api.domain.member.service.SmsService;
import com.latelier.api.domain.model.Result;
import com.latelier.api.domain.util.SecurityUtil;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final MeetingInformationService meetingInformationService;

    private final CourseService courseService;

    private final SmsService smsService;

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
            @ApiResponse(responseCode = "400", description = "등록하지 않은 강의"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않음"),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 입장정보를 찾을 수 없음")})
    public ResponseEntity<Result<ResMeetingInformation>> getMeeting(@PathVariable final Long courseId) {

        ResMeetingInformation response = meetingInformationService.getMeetingInformation(securityUtil.getMemberId(), courseId);
//        ResMeetingInformation response = meetingInformationService.getMeetingInformation(1L, courseId);
        return ResponseEntity.ok(Result.of(response));
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{courseId}/status")
    @ApiOperation(
            value = "강의를 미팅상태 체크",
            notes = "강의를 열기 전 이미 진행중인 강의인지, 혹은 강사가 다른 강의를 열어놓았는지 확인합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "현재 미팅 예약이 된 강의"),
            @ApiResponse(responseCode = "204", description = "미팅 예약이 되어있지 않음"),
            @ApiResponse(responseCode = "400", description = "강사의 강의가 아님"),
            @ApiResponse(responseCode = "403", description = "강사가 아님"),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 강의를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "강사의 다른 강의가 이미 진행중")})
    public ResponseEntity<Result<Map<String, String>>> checkCourseMeeting(@PathVariable Long courseId) {

        meetingInformationService.checkCourseAndMeeting(securityUtil.getMemberId(), courseId); // 404, 409
//        meetingInformationService.checkCourseAndMeeting(1L, courseId);
        // 강의 조회와 강의 미팅 url 반환
        String joinUrl = meetingInformationService.getJoinUrl(courseId);// 200, 204, 404(강의)
        if (StringUtils.hasText(joinUrl)) {
            Map<String, String> response = new HashMap<>();
            response.put("joinUrl", joinUrl);
            return ResponseEntity.ok(Result.of(response));
        }
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping(consumes = "multipart/form-data")
    @ApiOperation(
            value = "강의 등록(신청)",
            notes = "강의를 등록(신청)합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "강의등록(신청) 성공"),
            @ApiResponse(responseCode = "403", description = "강사가 아님"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "파일 입출력 실패 등 내부 서버에러")})
    public ResponseEntity<Result<ResCourseRegister>> registerCourse(@Valid final ReqCourseRegister reqCourseRegister) {

        ResCourseRegister response = courseService.addCourse(securityUtil.getMemberId(), reqCourseRegister);
//        ResCourseRegister response = courseService.addCourse(1L, reqCourseRegister);
        return ResponseEntity.status(CREATED)
                .body(Result.of(response));
    }


    @GetMapping
    @ApiOperation(
            value = "강의 검색",
            notes = "강의를 검색합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "강의 상태", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "search", value = "강의 검색어", required = false, dataTypeClass = String.class, paramType = "query")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강의목록 검색 성공")})
    public ResponseEntity<Page<ResCourseSimple>> searchCourse(@RequestParam(defaultValue = "APPROVED") final String state,
                                                              @RequestParam(required = false) final String search,
                                                              @Valid final Pageable pageable) {

        Page<ResCourseSimple> response = courseService.search(state, search, pageable);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{courseId}")
    @ApiOperation(
            value = "강의 상세보기",
            notes = "강의에 대한 상세정보를 반환합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강의 상세정보 반환 성공"),
            @ApiResponse(responseCode = "404", description = "강의를 찾지 못함")})
    public ResponseEntity<Result<ResCourseDetails>> getCourseDetails(@PathVariable final Long courseId) {

        Long memberId = null;
        if (securityUtil.isLoggedIn()) memberId = securityUtil.getMemberId();
        ResCourseDetails response = courseService.getCourseDetails(memberId, courseId);
        return ResponseEntity.ok(Result.of(response));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/approval/{courseId}")
    @ApiOperation(
            value = "강의 승인",
            notes = "등록 대기중인 강의를 승인합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "강의 승인 성공"),
            @ApiResponse(responseCode = "400", description = "이미 강의가 승인됨"),
            @ApiResponse(responseCode = "403", description = "관리자가 아님"),
            @ApiResponse(responseCode = "404", description = "강의를 찾지 못함")})
    public ResponseEntity<Void> approveCourse(@PathVariable final Long courseId) {

        courseService.approveCourse(courseId);
        return ResponseEntity.status(NO_CONTENT).build();
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{courseId}/sms-notification")
    @ApiOperation(
            value = "강의 시작 문자메세지 전송",
            notes = "수강생들에게 강의가 시작됨을 알리는 문자메세지를 전송합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전송 성공"),
            @ApiResponse(responseCode = "400", description = "강사의 강의가 아님"),
            @ApiResponse(responseCode = "400", description = "강의 수강생이 없음"),
            @ApiResponse(responseCode = "403", description = "강사가 아님"),
            @ApiResponse(responseCode = "404", description = "강사를 찾지 못함"),
            @ApiResponse(responseCode = "404", description = "강의를 찾지 못함")})
    public ResponseEntity<Void> noticeCourseStarting(@PathVariable final Long courseId) {

        List<Enrollment> enrollmentList = courseService.getEnrollmentList(securityUtil.getMemberId(), courseId);
//        List<Enrollment> enrollmentList = courseService.getEnrollmentList(1L, courseId);
        if (enrollmentList.size() == 0) {
            throw new BusinessException(ErrorCode.COURSE_STUDENT_EMPTY);
        }

        Map<String, String> toAndContent = new HashMap<>();
        for (Enrollment enrollment : enrollmentList) {
            String message = makeNotificationMessage(enrollment.getMember().getUsername(), enrollment.getCourse().getName());
            toAndContent.put(enrollment.getMember().getPhoneNumber(), message);
        }
        SmsService.ReqSms reqSms = smsService.makeSmsRequest(toAndContent);
        smsService.callApi(reqSms);
        return ResponseEntity.status(OK).build();
    }

    private String makeNotificationMessage(final String userName, final String courseName) {

        return "[Latelier] " + userName + "님의 \"" + courseName + "\" 수업이 시작되었습니다.";
    }
}