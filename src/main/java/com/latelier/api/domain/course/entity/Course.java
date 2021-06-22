package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "COURSE_SEQ_GENERATOR",
        sequenceName = "COURSE_SEQ",
        allocationSize = 1)
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COURSE_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", columnDefinition = "bigint")
    private Member teacher;

    @Column(length = 50)
    private String courseName;

    @Column(length = 500)
    private String explanation;

    @Column(columnDefinition = "int")
    private Integer price;

    @Column(columnDefinition = "int")
    private Integer currentSize;

    @Column(columnDefinition = "int")
    private Integer maxSize;

    private LocalDateTime endDate;


    @Builder
    private Course(final Member teacher, final String courseName,
                  final String explanation, final Integer price,
                  final Integer currentSize, final Integer maxSize, final LocalDateTime endDate) {

        this.teacher = teacher;
        this.courseName = courseName;
        this.explanation = explanation;
        this.price = price;
        this.currentSize = currentSize;
        this.maxSize = maxSize;
        this.endDate = endDate;
    }
}
