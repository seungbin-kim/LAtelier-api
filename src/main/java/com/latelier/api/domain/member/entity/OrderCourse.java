package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "ORDER_COURSE_SEQ_GENERATOR",
        sequenceName = "ORDER_COURSE_SEQ")
public class OrderCourse {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ORDER_COURSE_SEQ_GENERATOR")
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


    private OrderCourse(final Order order,
                        final Course course,
                        final Integer quantity,
                        final Integer price) {

        this.order = order;
        this.course = course;
        this.quantity = quantity;
        this.price = price;
    }


    public static OrderCourse of(final Order order,
                                 final Course course,
                                 final Integer quantity,
                                 final Integer price) {

        OrderCourse orderCourse = new OrderCourse(order, course, quantity, price);
        order.getOrderCourses().add(orderCourse);
        return orderCourse;
    }
}
