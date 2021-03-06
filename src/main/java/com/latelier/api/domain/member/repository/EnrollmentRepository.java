package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.member.entity.Enrollment;
import com.latelier.api.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByMemberIdAndCourseId(final Long memberId, final Long courseId);

    @EntityGraph(attributePaths = {"course"})
    Page<Enrollment> findByMember(Member member, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    List<Enrollment> findByCourse(Course course);

}
