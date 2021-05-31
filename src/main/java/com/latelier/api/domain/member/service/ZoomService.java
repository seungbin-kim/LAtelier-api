package com.latelier.api.domain.member.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.exception.CourseNotFoundException;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.course.service.MeetingInformationService;
import com.latelier.api.domain.member.exception.NotBeObtainAccessTokenException;
import com.latelier.api.domain.member.exception.NotBeObtainMeetingInformationException;
import com.latelier.api.domain.member.exception.ZoomAccessTokenRequestException;
import com.latelier.api.domain.member.exception.ZoomApiRequestException;
import com.latelier.api.domain.member.packet.request.ReqZoomMeeting;
import com.latelier.api.domain.member.packet.response.ResZoomMeeting;
import com.latelier.api.domain.member.packet.response.ResZoomOAuthToken;
import com.latelier.api.global.properties.ZoomProperties;
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
  public String createCourseMeeting(final String code,
                                    final Long courseId) {

    // TODO 로그인 구현 후, 강사의 강의가 맞는지 확인하는 로직 필요

    Optional<Course> byCourseId = courseRepository.findById(courseId);
    Course course = byCourseId.orElseThrow(() -> new CourseNotFoundException(courseId));

    String accessToken = requestAccessToken(code)
        .orElseThrow(NotBeObtainAccessTokenException::new);

    ResZoomMeeting resZoomMeeting = requestMeetingCreation(accessToken, course.getCourseName())
        .orElseThrow(NotBeObtainMeetingInformationException::new);

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

    ReqZoomMeeting reqZoomMeeting = ReqZoomMeeting.createReqZoomMeeting(courseName);
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
