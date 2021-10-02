package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByMemberIdAndCourseId(final Long memberId, final Long courseId);

}
