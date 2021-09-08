package com.latelier.api.domain.course.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "COURSE_CATEGORY_SEQ_GENERATOR",
        sequenceName = "COURSE_CATEGORY_SEQ")
public class CourseCategory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COURSE_CATEGORY_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", columnDefinition = "bigint")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", columnDefinition = "bigint")
    private Category category;


    private CourseCategory(final Course course,
                           final Category category) {

        this.category = category;
        this.course = course;
    }

    public static CourseCategory of(final Course course,
                                    final Category category) {

        return new CourseCategory(course, category);
    }

}
