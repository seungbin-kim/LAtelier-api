package com.latelier.api.domain.order.repository;

import com.latelier.api.domain.order.entity.OrderCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCourseRepository extends JpaRepository<OrderCourse, Long> {
}
