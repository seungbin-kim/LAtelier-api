package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.MeetingInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingInformationRepository extends JpaRepository<MeetingInformation, Long> {

  Optional<MeetingInformation> findByCourseId(final Long courseId);

}
