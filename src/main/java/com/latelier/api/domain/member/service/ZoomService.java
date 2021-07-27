package com.latelier.api.domain.member.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.exception.CourseNotFoundException;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.member.exception.AccessTokenNotBeObtainedException;
import com.latelier.api.domain.member.exception.MeetingInformationNotBeObtainedException;
import com.latelier.api.domain.member.exception.ZoomAccessTokenRequestException;
import com.latelier.api.domain.member.exception.ZoomApiRequestException;
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
    @Transactional
    public String createCourseMeeting(final String code, final Long courseId) {

        // TODO 로그인 구현 후, 강사의 강의가 맞는지 확인하는 로직 필요

        Optional<Course> byCourseId = courseRepository.findById(courseId);
        Course course = byCourseId.orElseThrow(() -> new CourseNotFoundException(courseId));

        String accessToken = requestAccessToken(code)
                .orElseThrow(AccessTokenNotBeObtainedException::new);

        ResZoomMeeting resZoomMeeting = requestMeetingCreation(accessToken, course.getCourseName())
                .orElseThrow(MeetingInformationNotBeObtainedException::new);

        meetingInformationService.addMeetingInformation(
                course,
                resZoomMeeting.getId(),
                resZoomMeeting.getPassword());

        return resZoomMeeting.getStart_url();
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
            throw new ZoomApiRequestException();
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

            return Optional.ofNullable(zoomOAuthToken).map(ResZoomOAuthToken::getAccess_token);
        } catch (Exception e) {
            throw new ZoomAccessTokenRequestException();
        }
    }

}


@Getter
@EqualsAndHashCode
class ResZoomOAuthToken {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private int expires_in;

    private String scope;

}


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ReqZoomMeeting {

    private final String topic;

    private final int type;

    private final String timezone;


    public static ReqZoomMeeting of(final String topic) {

        return new ReqZoomMeeting(topic, 1, "Asia/Seoul");
    }

}


@Getter
@EqualsAndHashCode
class ResZoomMeeting {

    private String created_at;
    private String encrypted_password;
    private String h323_password;
    private String host_email;
    private String host_id;
    private String id;
    private String join_url;
    private String password;
    private String pstn_password;
    private Settings settings;
    private String start_url;
    private String status;
    private String timezone;
    private String topic;
    private Integer type;
    private String uuid;

    @Getter
    @EqualsAndHashCode
    public class Settings {

        private Boolean allow_multiple_devices;
        private String alternative_hosts;
        private Integer approval_type;
        private ApprovedOrDeniedCountriesOrRegions approved_or_denied_countries_or_regions;
        private String audio;
        private String auto_recording;
        private BreakoutRoom breakout_room;
        private Boolean close_registration;
        private Boolean cn_meeting;
        private Boolean device_testing;
        private String encryption_type;
        private Boolean enforce_login;
        private String enforceLogin_domains;
        private Boolean host_video;
        private Boolean in_meeting;
        private Integer jbh_time;
        private Boolean join_beforeHost;
        private Boolean meeting_authentication;
        private Boolean mute_upon_entry;
        private Boolean participant_video;
        private Boolean registrants_confirmation_email;
        private Boolean registrants_email_notification;
        private Boolean request_permission_to_unmute_participants;
        private Boolean show_share_button;
        private Boolean use_pmi;
        private Boolean waiting_room;
        private Boolean watermark;

        @Getter
        @EqualsAndHashCode
        public class ApprovedOrDeniedCountriesOrRegions {

            private Boolean enable;

        }

        @Getter
        @EqualsAndHashCode
        public class BreakoutRoom {

            private Boolean enable;

        }

    }

}