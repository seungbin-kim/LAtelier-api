package com.latelier.api.domain.order.entity;

import com.latelier.api.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "ORDER_ITEM_SEQ_GENERATOR",
    sequenceName = "ORDER_ITEM_SEQ",
    allocationSize = 1)
public class OrderCourse {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "ORDER_ITEM_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", columnDefinition = "bigint")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", columnDefinition = "bigint")
  private Course course;

  @Column(columnDefinition = "int")
  private Integer quantity;

  @Column(columnDefinition = "int")
  private Integer price;

}
