package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.enumuration.CourseState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c " +
            "FROM Course c " +
            "JOIN FETCH c.instructor m " +
            "WHERE c.id = :courseId")
    Optional<Course> findWithInstructorById(@Param("courseId") final Long courseId);

    Optional<Course> findByIdAndStateLike(final Long id, final CourseState state);

}
