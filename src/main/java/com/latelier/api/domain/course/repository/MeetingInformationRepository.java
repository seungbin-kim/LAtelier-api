package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.MeetingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface MeetingInformationRepository extends JpaRepository<MeetingInformation, Long> {

  Optional<MeetingInformation> findByCourseId(Long courseId);

  boolean existsByCourseIn(List<Course> courseList);

  @Modifying
  void deleteByMeetingId(String meetingId);

}
