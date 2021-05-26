package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
