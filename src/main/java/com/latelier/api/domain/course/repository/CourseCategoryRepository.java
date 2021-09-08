package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {

    @Query("SELECT cc " +
            "FROM CourseCategory cc " +
            "JOIN FETCH cc.category c " +
            "WHERE cc.course = :course")
    List<CourseCategory> findAllWithCategoryByCourse(@Param("course") final Course course);

}
