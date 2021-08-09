package com.latelier.api.domain.util;

import com.latelier.api.global.properties.NaverProperties;
import com.latelier.api.global.properties.ZoomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignatureGenerator {

  private final ZoomProperties zoomProperties;

  private final NaverProperties naverProperties;

  private final SecretKeySpec sdkSigningKey;

  private final SecretKeySpec smsSigningKey;

  /**
   * Web SDK 입장시 필요한 시그니처 생성
   *
   * @param meetingNumber 회의 ID
   * @return 생성된 시그니처
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeyException
   */
  public String generateSignatureForZoomSDK(final String meetingNumber) {
    try {
      String apiKey = zoomProperties.getApi().getKey();

      Mac hasher = Mac.getInstance("HmacSHA256");
      String ts = Long.toString(System.currentTimeMillis() - 30000);
      String msg = String.format("%s%s%s%d", apiKey, meetingNumber, ts, 0);

      hasher.init(sdkSigningKey);

      String message = Base64.getEncoder().encodeToString(msg.getBytes());
      byte[] hash = hasher.doFinal(message.getBytes());

      String hashBase64Str = DatatypeConverter.printBase64Binary(hash);
      String tmpString = String.format("%s.%s.%s.%d.%s", apiKey, meetingNumber, ts, 0, hashBase64Str);
      String encodedString = Base64.getEncoder().encodeToString(tmpString.getBytes());

      return encodedString.replaceAll("=+$", "");
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      log.error(e.getMessage());
      throw new SignatureGenerationException();
    }
  }


  /**
   * SMS 전송을 위한 시그니처 생성
   * @param time 요청시간
   * @return 생성된 시그니처
   */
  public String generateSignatureForSms(final String time) {
    try {
      String space = " ";
      String newLine = "\n";
      String method = "POST";
      String url = "/sms/v2/services/" + naverProperties.getCloudPlatform().getSens().getServiceId() + "/messages";
      String accessKey = naverProperties.getCloudPlatform().getKey();

      String message = new StringBuilder()
          .append(method)
          .append(space)
          .append(url)
          .append(newLine)
          .append(time)
          .append(newLine)
          .append(accessKey)
          .toString();

      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(smsSigningKey);

      byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
      return Base64.getEncoder().encodeToString(rawHmac);
    } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
      log.error(e.getMessage());
      throw new SignatureGenerationException();
    }
  }

}
