package com.latelier.api.domain.course.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.properties.ZoomProperties;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZoomService {

    private final ZoomProperties zoomProperties;

    private final RestTemplate restTemplate;

    private final CourseRepository courseRepository;

    private final MeetingInformationService meetingInformationService;


    /**
     * 회의생성
     *
     * @param code     Authorization Code
     * @param courseId Course ID
     * @return redirect url
     */
    @Transactional(rollbackFor = Exception.class)
    public String createCourseMeeting(final String code,
                                      final Long courseId) {

        // 존재하는 강의인가?
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_NOT_FOUND));
        // 사용자 액세스토큰 얻기
        String accessToken = requestAccessToken(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCESS_TOKEN_NOT_OBTAIN));
        // 액세스 토큰으로 회의생성
        ResZoomMeeting resZoomMeeting = requestMeetingCreation(accessToken, course.getName())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_INFORMATION_NOT_OBTAIN));
        // 회의정보 저장
        meetingInformationService.addMeetingInformation(
                course,
                resZoomMeeting.getId(),
                resZoomMeeting.getPassword(),
                resZoomMeeting.getJoinUrl());

        return resZoomMeeting.getStartUrl();
    }


    /**
     * 액세스 토큰을 사용하여 회의생성 API 호출
     *
     * @param accessToken 강사의 액세스 토큰
     * @param courseName  강의 이름
     * @return 회의생성 API 응답
     */
    private Optional<ResZoomMeeting> requestMeetingCreation(final String accessToken,
                                                            final String courseName) {

        String meetingCreationUrl = zoomProperties.getUrl().getMeetingCreation();

        ReqZoomMeeting reqZoomMeeting = ReqZoomMeeting.of(courseName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqZoomMeeting> zoomMeetingRequestHttpEntity = new HttpEntity<>(reqZoomMeeting, httpHeaders);

        try {
            ResZoomMeeting resZoomMeeting = restTemplate.postForObject(
                    meetingCreationUrl,
                    zoomMeetingRequestHttpEntity,
                    ResZoomMeeting.class);

            return Optional.ofNullable(resZoomMeeting);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ZOOM_API_CALL_FAILED);
        }
    }


    /**
     * 액세스토큰 요청
     *
     * @param code Authorization Code
     * @return Access Token
     */
    private Optional<String> requestAccessToken(final String code) {

        String prod = zoomProperties.getOauthApp().getProd();
        String oauthUrl = zoomProperties.getUrl().getOauth();
        String redirectUrl = zoomProperties.getUrl().getRedirect();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + prod);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(oauthUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", code)
                .queryParam("redirect_uri", redirectUrl)
                .build();

        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        try {
            ResZoomOAuthToken zoomOAuthToken = restTemplate.postForObject(
                    uriComponents.toUri(),
                    entity,
                    ResZoomOAuthToken.class);

            return Optional.ofNullable(zoomOAuthToken).map(ResZoomOAuthToken::getAccessToken);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ACCESS_TOKEN_REQUEST_FAILED);
        }
    }


    @Getter
    @EqualsAndHashCode
    static class ResZoomOAuthToken {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("expires_in")
        private int expiresIn;

        private String scope;

    }


    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class ReqZoomMeeting {

        private final String topic;

        private final int type;

        private final String timezone;


        public static ReqZoomMeeting of(final String topic) {

            return new ReqZoomMeeting(topic, 1, "Asia/Seoul");
        }

    }


    @Getter
    @EqualsAndHashCode
    static class ResZoomMeeting {

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("encrypted_password")
        private String encryptedPassword;

        @JsonProperty("h323_password")
        private String h323Password;

        @JsonProperty("host_email")
        private String hostEmail;

        @JsonProperty("host_id")
        private String hostId;

        private String id;

        @JsonProperty("join_url")
        private String joinUrl;

        private String password;

        @JsonProperty("pstn_password")
        private String pstnPassword;

        private Settings settings;

        @JsonProperty("start_url")
        private String startUrl;

        private String status;
        private String timezone;
        private String topic;
        private Integer type;
        private String uuid;

        @Getter
        @EqualsAndHashCode
        public static class Settings {

            @JsonProperty("allow_multiple_devices")
            private Boolean allowMultipleDevices;

            @JsonProperty("alternative_hosts")
            private String alternativeHosts;

            @JsonProperty("approval_type")
            private Integer approvalType;

            @JsonProperty("approved_or_denied_countries_or_regions")
            private ApprovedOrDeniedCountriesOrRegions approvedOrDeniedCountriesOrRegions;

            private String audio;

            @JsonProperty("auto_recording")
            private String autoRecording;

            @JsonProperty("breakout_room")
            private BreakoutRoom breakoutRoom;

            @JsonProperty("close_registration")
            private Boolean closeRegistration;

            @JsonProperty("cn_meeting")
            private Boolean cnMeeting;

            @JsonProperty("device_testing")
            private Boolean deviceTesting;

            @JsonProperty("encryption_type")
            private String encryptionType;

            @JsonProperty("enforce_login")
            private Boolean enforceLogin;

            @JsonProperty("enforceLogin_domains")
            private String enforceLoginDomains;

            @JsonProperty("host_video")
            private Boolean hostVideo;

            @JsonProperty("in_meeting")
            private Boolean inMeeting;

            @JsonProperty("jbh_time")
            private Integer jbhTime;

            @JsonProperty("join_beforeHost")
            private Boolean joinBeforeHost;

            @JsonProperty("meeting_authentication")
            private Boolean meetingAuthentication;

            @JsonProperty("mute_upon_entry")
            private Boolean muteUponEntry;

            @JsonProperty("participant_video")
            private Boolean participantVideo;

            @JsonProperty("registrants_confirmation_email")
            private Boolean registrantsConfirmationEmail;

            @JsonProperty("registrants_email_notification")
            private Boolean registrantsEmailNotification;

            @JsonProperty("request_permission_to_unmute_participants")
            private Boolean requestPermissionToUnmuteParticipants;

            @JsonProperty("show_share_button")
            private Boolean showShareButton;

            @JsonProperty("use_pmi")
            private Boolean usePmi;

            @JsonProperty("waiting_room")
            private Boolean waitingRoom;

            private Boolean watermark;

            @Getter
            @EqualsAndHashCode
            public static class ApprovedOrDeniedCountriesOrRegions {

                private Boolean enable;

            }

            @Getter
            @EqualsAndHashCode
            public static class BreakoutRoom {

                private Boolean enable;

            }

        }

    }

}