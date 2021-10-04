package com.latelier.api.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.course.service.ZoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/zoom")
public class ZoomController {

    private final ZoomService zoomService;

    private final MeetingInformationService meetingInformationService;


    //    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/callback")
    @ApiOperation(
            value = "Zoom OAuth 인증과 회의생성 API 호출",
            notes = "Zoom OAuth 인증 후 회의생성 API 를 호출하여 start url 을 반환합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "authorization code", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "회의를 생성할 강의 ID", required = true, dataTypeClass = Long.class, paramType = "query")})
    @ApiResponses({
            @ApiResponse(responseCode = "303", description = "성공적으로 회의가 생성되어 start url 반환"),
            @ApiResponse(responseCode = "403", description = "강사가 아님"),
            @ApiResponse(responseCode = "500", description = "액세스 토큰을 얻지 못하거나 회의 생성에 실패")})
    public ResponseEntity<Void> callback(@RequestParam final String code,
                                         @RequestParam(name = "state") final Long courseId) throws URISyntaxException {

        String startUrl = zoomService.createCourseMeeting(code, courseId);

        URI uri = new URI(startUrl);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(uri).build();
    }


    @PostMapping("/event")
    @ApiOperation(
            value = "Zoom 이벤트를 받음",
            notes = "Zoom 으로부터 이벤트를 받는 End-Point")
    public void zoomEvent(@RequestBody final ZoomEvent zoomEvent) {

        String meetingId = zoomEvent.getPayload().getObject().getId();
        meetingInformationService.deleteMeetingInformation(meetingId);
    }


    @Getter
    @ToString
    static class ZoomEvent {

        private String event;

        @JsonProperty("event_ts")
        private Long eventTs;

        private PayLoad payload;

        @Getter
        @ToString
        public static class PayLoad {

            @JsonProperty("account_id")
            private String accountId;

            private Object object;

            @Getter
            @ToString
            public static class Object {

                private String uuid;

                private String id;

                @JsonProperty("host_id")
                private String hostId;

                private String topic;

                private Integer type;

                @JsonProperty("start_time")
                private String startTime;

                private Integer duration;

                private String timezone;

                private String endTime;

            }

        }

    }

}