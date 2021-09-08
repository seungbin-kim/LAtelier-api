package com.latelier.api.domain.file.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.file.entity.CourseFile;
import com.latelier.api.domain.file.enumuration.FileGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseFileRepository extends JpaRepository<CourseFile, Long> {

    @Query("SELECT cf " +
            "FROM CourseFile cf " +
            "JOIN FETCH cf.file cff " +
            "WHERE cff.fileGroup = :fileGroup " +
            "AND cf.course IN :courses")
    List<CourseFile> findImageFileByCourses(@Param("fileGroup") final FileGroup fileGroup,
                                            @Param("courses") final List<Course> courses);

    @Query("SELECT cf " +
            "FROM CourseFile cf " +
            "JOIN FETCH cf.file " +
            "WHERE cf.course = :course")
    List<CourseFile> findAllWithFileByCourse(@Param("course") final Course course);

}
