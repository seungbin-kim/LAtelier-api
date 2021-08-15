package com.latelier.api.domain.file.repository;

import com.latelier.api.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
