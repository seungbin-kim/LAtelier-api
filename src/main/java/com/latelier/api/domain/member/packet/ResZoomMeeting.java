package com.latelier.api.domain.member.packet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ResZoomMeeting {

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
