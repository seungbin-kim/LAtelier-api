package com.latelier.api.domain.member.packet;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReqSms {

  private final String type;

  private final String from;

  private final String content;

  private final List<Message> messages;

  private String countryCode;

  private String contentType;


  public static ReqSms createSmsRequest(final String from,
                                 final String content,
                                 final List<Message> messages) {

    return new ReqSms("SMS", from, content, messages);
  }


  @Getter
  @Setter
  @EqualsAndHashCode
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Message {

    private final String to;

    private String content;


    public static Message createMessage(final String to) {

      return new Message(to);
    }

  }

}
