package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.packet.ReqZoomMeeting;
import com.latelier.api.domain.member.packet.ReqZoomOAuthToken;
import com.latelier.api.domain.member.packet.ResZoomMeeting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ZoomService {

  private final String zoomKey;

  private final String zoomOAuthUrl;

  private final String zoomCreateMeetingUrl;

  private final String redirectUrl;

  private final RestTemplate restTemplate;

  public ZoomService(
      @Value("${zoom.app.prod}") final String zoomKey,
      @Value("${zoom.url.oauth}") final String zoomOAuthUrl,
      @Value("${zoom.url.create-meeting}") final String zoomCreateMeetingUrl,
      @Value("${zoom.url.redirect}") final String redirectUrl,
      final RestTemplate restTemplate) {

    this.zoomKey = zoomKey;
    this.zoomOAuthUrl = zoomOAuthUrl;
    this.zoomCreateMeetingUrl = zoomCreateMeetingUrl;
    this.redirectUrl = redirectUrl;
    this.restTemplate = restTemplate;
  }

  /**
   * 회의생성 API 호출
   * @param code Authorization Code
   * @return redirect url
   */
  public String createCourseMeeting(final String code) {
    String accessToken = requestAccessToken(code);

    ReqZoomMeeting reqZoomMeeting = new ReqZoomMeeting("test");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + accessToken);
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<ReqZoomMeeting> zoomMeetingRequestHttpEntity = new HttpEntity<>(reqZoomMeeting, httpHeaders);

    ResZoomMeeting resZoomMeeting = restTemplate.postForObject(
        zoomCreateMeetingUrl,
        zoomMeetingRequestHttpEntity,
        ResZoomMeeting.class);

    return resZoomMeeting != null ? resZoomMeeting.getStart_url() : null;
  }

  /**
   * 액세스토큰 요청
   * @param code Authorization Code
   * @return Access Token
   */
  private String requestAccessToken(final String code) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Basic " + zoomKey);

    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(zoomOAuthUrl)
        .queryParam("grant_type", "authorization_code")
        .queryParam("code", code)
        .queryParam("redirect_uri", redirectUrl)
        .build();

    HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

    ReqZoomOAuthToken zoomOAuthToken = restTemplate.postForObject(
        uriComponents.toUri(),
        entity,
        ReqZoomOAuthToken.class);

    return zoomOAuthToken != null ? zoomOAuthToken.getAccess_token() : null;
  }

}
