package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.OrderCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCourseRepository extends JpaRepository<OrderCourse, Long> {
}
