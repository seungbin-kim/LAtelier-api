package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
