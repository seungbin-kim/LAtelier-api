package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
}
