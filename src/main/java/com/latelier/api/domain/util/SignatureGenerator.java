package com.latelier.api.domain.util;

import com.latelier.api.global.properties.ZoomProperties;
import com.latelier.api.global.error.exception.SignatureGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignatureGenerator {

  private final ZoomProperties zoomProperties;

  public String generateSignature(final String meetingNumber) {
    try {
      String apiKey = zoomProperties.getApi().getKey();
      String apiSecret = zoomProperties.getApi().getSecret();

      Mac hasher = Mac.getInstance("HmacSHA256");
      String ts = Long.toString(System.currentTimeMillis() - 30000);
      String msg = String.format("%s%s%s%d", apiKey, meetingNumber, ts, 0);

      hasher.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256"));

      String message = Base64.getEncoder().encodeToString(msg.getBytes());
      byte[] hash = hasher.doFinal(message.getBytes());

      String hashBase64Str = DatatypeConverter.printBase64Binary(hash);
      String tmpString = String.format("%s.%s.%s.%d.%s", apiKey, meetingNumber, ts, 0, hashBase64Str);
      String encodedString = Base64.getEncoder().encodeToString(tmpString.getBytes());

      return encodedString.replaceAll("\\=+$", "");
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      log.error(e.getMessage());
      throw new SignatureGenerationException();
    }
  }

}
