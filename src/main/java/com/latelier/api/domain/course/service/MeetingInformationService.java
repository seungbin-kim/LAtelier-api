package com.latelier.api.domain.course.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.MeetingInformation;
import com.latelier.api.domain.course.exception.CourseMeetingNotFoundException;
import com.latelier.api.domain.course.packet.response.ResMeetingInformation;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.course.repository.MeetingInformationRepository;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.repository.EnrollmentRepository;
import com.latelier.api.domain.member.repository.MemberRepository;
import com.latelier.api.domain.util.SignatureGenerator;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.properties.ZoomProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.latelier.api.domain.course.enumuration.CourseState.APPROVED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingInformationService {

    private final ZoomProperties zoomProperties;

    private final MeetingInformationRepository meetingInformationRepository;

    private final MemberRepository memberRepository;

    private final CourseRepository courseRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final SignatureGenerator signatureGenerator;


    /**
     * 강사의 생성된 회의(강의)정보를 저장합니다.
     *
     * @param course    강사의 강의
     * @param meetingId 생성된 회의 ID
     * @param meetingPw 생성된 회의 비밀번호
     */
    @Transactional
    public void addMeetingInformation(final Course course,
                                      final String meetingId,
                                      final String meetingPw,
                                      final String joinUrl) {

        meetingInformationRepository.save(MeetingInformation.of(course, meetingId, meetingPw, joinUrl));
    }


    /**
     * 미팅정보 삭제
     *
     * @param meetingId Meeting ID
     */
    @Transactional
    public void deleteMeetingInformation(final String meetingId) {

        meetingInformationRepository.deleteByMeetingId(meetingId);
    }


    /**
     * Web SDK 에서 접속하기위한 회의정보 얻기
     *
     * @param memberId 사용자 ID
     * @param courseId 강의 ID
     * @return 회의정보
     */
    public ResMeetingInformation getMeetingInformation(final Long memberId,
                                                       final Long courseId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!enrollmentRepository.existsByMemberIdAndCourseId(memberId, courseId)) {
            throw new BusinessException(ErrorCode.NOT_ENROLLED);
        }

        String phoneNumber = member.getPhoneNumber();
        String last4Digits = phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
        return meetingInformationRepository.findByCourseId(courseId)
                .map(meetingInformation -> ResMeetingInformation.of(
                        zoomProperties.getApi().getKey(),
                        meetingInformation,
                        member.getUsername() + last4Digits,
                        signatureGenerator.generateSignatureForZoomSDK(meetingInformation.getMeetingId())))
                .orElseThrow(() -> new CourseMeetingNotFoundException(courseId));
    }


    /**
     * 강사의 강의가 맞는지 확인
     * 강사가 이미 진행중인 강의가 존재하는지 확인
     *
     * @param instructorId 강사 ID
     */
    public void checkCourseAndMeeting(final Long instructorId,
                                      final Long courseId) {

        // 사용자 존재여부 확인
        Member currentMember = memberRepository.findById(instructorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        // 강의 존재여부 확인
        Course course = courseRepository.findByIdAndStateLike(courseId, APPROVED)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_NOT_FOUND));
        // 현재 강사의 강의인지 확인
        if (!currentMember.getId().equals(course.getInstructor().getId())) {
            throw new BusinessException(ErrorCode.COURSE_INSTRUCTOR_NOT_MATCH);
        }
        // 현재 강사가 이미 진행중인 다른 강의가 존재하는지 확인
        List<Course> courses = courseRepository.findAllByInstructorAndIdIsNotAndStateLike(currentMember, courseId, APPROVED);
        if (meetingInformationRepository.existsByCourseIn(courses)) {
            throw new BusinessException(ErrorCode.COURSE_MEETING_ALREADY_EXIST);
        }
    }


    /**
     * 강사 접속을 위한 Join URL 얻기
     *
     * @param courseId 강의 ID
     * @return 진행중인 강의 URL. 진행중이 아니라면 빈 문자열
     */
    public String getJoinUrl(final Long courseId) {

        String joinUrl = "";
        Optional<MeetingInformation> information = meetingInformationRepository.findByCourseId(courseId);
        if (information.isPresent()) {
            joinUrl = information.get().getJoinUrl();
        }
        return joinUrl;
    }

}
