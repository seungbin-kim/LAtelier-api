package com.latelier.api.domain.course.packet.response;

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
            name = "apiKey")
    private final String apiKey;

    @ApiModelProperty(
            value = "미팅 번호",
            name = "meetingNumber",
            notes = "미팅 입장에 필요한 ID")
    private final Long meetingNumber;

    @ApiModelProperty(
            value = "미팅 비밀번호",
            name = "meetingPassword")
    private final String meetingPassword;

    @ApiModelProperty(
            value = "이름",
            name = "username")
    private final String username;

    @ApiModelProperty(
            value = "Signature",
            name = "signature")
    private final String signature;


    public static ResMeetingInformation of(final String apiKey,
                                           final MeetingInformation meetingInformation,
                                           final String userName,
                                           final String signature) {

        return new ResMeetingInformation(
                apiKey,
                Long.parseLong(meetingInformation.getMeetingId()),
                meetingInformation.getMeetingPw(),
                userName,
                signature);
    }

}
