package com.latelier.api.domain.course.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.MeetingInformation;
import com.latelier.api.domain.course.exception.CourseMeetingNotFoundException;
import com.latelier.api.domain.course.packet.response.ResMeetingInformation;
import com.latelier.api.domain.course.repository.MeetingInformationRepository;
import com.latelier.api.domain.util.SignatureGenerator;
import com.latelier.api.global.properties.ZoomProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingInformationService {

    private final ZoomProperties zoomProperties;

    private final MeetingInformationRepository meetingInformationRepository;

    private final SignatureGenerator signatureGenerator;


    /**
     * 강사의 생성된 회의(강의)정보를 저장합니다.
     *
     * @param course    강사의 강의
     * @param meetingId 생성된 회의 ID
     * @param meetingPw 생성된 회의 비밀번호
     */
    public void addMeetingInformation(final Course course,
                                      final String meetingId,
                                      final String meetingPw) {

        meetingInformationRepository.save(createMeetingInformation(course, meetingId, meetingPw));
    }


    /**
     * 회의정보 생성
     *
     * @param course    강의
     * @param meetingId 회의 ID
     * @param meetingPw 회의 비밀번호
     * @return 생성된 회의정보
     */
    private MeetingInformation createMeetingInformation(final Course course,
                                                        final String meetingId,
                                                        final String meetingPw) {

        return MeetingInformation.of(course, meetingId, meetingPw);
    }


    // TODO 로그인 구현후, 사용자 이름부분 로그인된 유저로 넣기

    /**
     * Web SDK 에서 접속하기위한 회의정보 얻기
     *
     * @param courseId 강의 ID
     * @return 회의정보
     */
    public ResMeetingInformation getMeetingInformation(final Long courseId) {

        return meetingInformationRepository.findByCourseId(courseId)
                .map(m -> ResMeetingInformation.of(
                        zoomProperties.getApi().getKey(),
                        m,
                        "테스트",
                        signatureGenerator.generateSignatureForZoomSDK(m.getMeetingId())))
                .orElseThrow(() -> new CourseMeetingNotFoundException(courseId));
    }

}
