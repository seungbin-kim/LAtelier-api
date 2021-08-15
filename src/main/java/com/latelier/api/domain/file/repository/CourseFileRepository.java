package com.latelier.api.domain.file.repository;

import com.latelier.api.domain.file.entity.CourseFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseFileRepository extends JpaRepository<CourseFile, Long> {
}
