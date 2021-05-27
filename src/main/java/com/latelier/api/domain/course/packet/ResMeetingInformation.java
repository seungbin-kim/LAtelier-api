package com.latelier.api.domain.course.packet;

import com.latelier.api.domain.course.entity.MeetingInformation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("강의 미팅정보")
public class ResMeetingInformation {

  @ApiModelProperty(
      value = "API KEY",
      name = "apiKey",
      notes = "SDK 사용에 필요한 API KEY")
  private final String apiKey;

  @ApiModelProperty(
      value = "미팅 번호",
      name = "meetingNumber",
      notes = "미팅 입장에 필요한 ID")
  private final Long meetingNumber;

  @ApiModelProperty(
      value = "미팅 비밀번호",
      name = "meetingPassword",
      notes = "미팅 입장에 필요한 비밀번호")
  private final String meetingPassword;

  @ApiModelProperty(
      value = "이름",
      name = "userName",
      notes = "학생의 실명")
  private final String userName;

  @ApiModelProperty(
      value = "Signature",
      name = "signature",
      notes = "Client Web SDK 사용시 필요한 시그니처")
  private final String signature;


  public static ResMeetingInformation createMeetingInformationForSDK(final String apiKey,
                                                                     final MeetingInformation meetingInformation,
                                                                     final String userName,
                                                                     final String signature) {

    return
        new ResMeetingInformation(
            apiKey,
            Long.parseLong(meetingInformation.getMeetingId()),
            meetingInformation.getMeetingPw(),
            userName,
            signature);
  }

}
