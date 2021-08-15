package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.course.enumuration.CourseState;
import com.latelier.api.domain.file.entity.CourseFile;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Integer currentSize = 0;

    @Column(columnDefinition = "int")
    private Integer maxSize;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CourseFile> courseFiles = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseCategory> courseCategories = new ArrayList<>();

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CourseState state = CourseState.WAITING;


    private Course(final Member instructor,
                   final String courseName,
                   final String explanation,
                   final Integer price,
                   final Integer maxSize,
                   final LocalDateTime startDate,
                   final LocalDateTime endDate) {

        this.instructor = instructor;
        this.courseName = courseName;
        this.explanation = explanation;
        this.price = price;
        this.maxSize = maxSize;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public static Course of(final Member instructor,
                            final String courseName,
                            final String explanation,
                            final Integer price,
                            final Integer maxSize,
                            final LocalDateTime startDate,
                            final LocalDateTime endDate) {

        return new Course(
                instructor,
                courseName,
                explanation,
                price,
                maxSize,
                startDate,
                endDate);
    }


    // TODO 빌더패턴 지우기. 테스트도 고치기
    public static CourseBuilder builder(final Member instructor,
                                        final String courseName) {

        return new CourseBuilder().instructor(instructor).courseName(courseName);
    }

}
