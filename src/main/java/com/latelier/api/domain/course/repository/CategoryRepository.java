package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNullOrderById();

}
