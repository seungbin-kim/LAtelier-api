package com.latelier.api.domain.course.controller;

import com.latelier.api.domain.course.packet.response.ResMeetingInformation;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.model.ResultResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseController {

  private final MeetingInformationService meetingInformationService;


  @PostMapping("/v1/courses/{courseId}")
  @ApiOperation(
      value = "강의 입장정보 요청",
      notes = "SDK 에서 강의에 입장하기위한 정보를 요청합니다.")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "courseId", value = "강의 ID", required = true, dataType = "long", paramType = "path", example = "1")})
  public ResponseEntity<?> getMeetingV1(@PathVariable final Long courseId) {

    return
        ResponseEntity.ok(
            ResultResponse.<ResMeetingInformation>builder()
                .content(meetingInformationService.getMeetingInformation(courseId))
                .build()
        );
  }

}
