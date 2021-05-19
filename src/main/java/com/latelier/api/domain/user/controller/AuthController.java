package com.latelier.api.domain.user.controller;

import com.latelier.api.domain.user.service.ZoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final ZoomService zoomService;

  /**
   * OAuth 인증과 회의생성 API 호출 후 start url 을 돌려줍니다.
   * @param code Authorization Code
   * @return start url
   */
  @GetMapping("/zoom/callback")
  public ResponseEntity<?> zoomCallback(@RequestParam final String code) {
    String startUrl = zoomService.createCourseMeeting(code);
    URI uri = null;
    try {
      uri = new URI(startUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.SEE_OTHER).location(uri).build();
  }

}
