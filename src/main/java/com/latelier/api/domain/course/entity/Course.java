package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.course.enumuration.CourseState;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "COURSE_SEQ_GENERATOR",
        sequenceName = "COURSE_SEQ")
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COURSE_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", columnDefinition = "bigint", nullable = false)
    private Member instructor;

    @Column(length = 50, nullable = false)
    private String courseName;

    @Column(length = 500)
    private String explanation;

    @Column(columnDefinition = "int")
    private Integer price;

    @Column(columnDefinition = "int")
    private Integer currentSize;

    @Column(columnDefinition = "int")
    private Integer maxSize;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CourseState state;


    public static CourseBuilder builder(final Member instructor,
                                        final String courseName) {

        return new CourseBuilder().instructor(instructor).courseName(courseName);
    }

}
