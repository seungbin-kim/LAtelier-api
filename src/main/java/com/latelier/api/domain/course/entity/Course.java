package com.latelier.api.domain.course.entity;

import com.latelier.api.domain.course.enumuration.CourseState;
import com.latelier.api.domain.file.entity.CourseFile;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
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
    private String name;

    @Column(length = 500)
    private String explanation;

    @Column(columnDefinition = "int")
    private Integer price;

    @Column(columnDefinition = "int")
    private Integer currentHeadCount = 0;

    @Column(columnDefinition = "int")
    private Integer maxHeadCount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<CourseFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseCategory> categories = new ArrayList<>();

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CourseState state = CourseState.WAITING;


    private Course(final Member instructor,
                   final String name,
                   final String explanation,
                   final Integer price,
                   final Integer maxHeadCount,
                   final LocalDateTime startDate,
                   final LocalDateTime endDate) {

        this.instructor = instructor;
        this.name = name;
        this.explanation = explanation;
        this.price = price;
        this.maxHeadCount = maxHeadCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public static Course of(final Member instructor,
                            final String name,
                            final String explanation,
                            final Integer price,
                            final Integer maxHeadCount,
                            final LocalDateTime startDate,
                            final LocalDateTime endDate) {

        return new Course(
                instructor,
                name,
                explanation,
                price,
                maxHeadCount,
                startDate,
                endDate);
    }


    public void changeState(final CourseState newState) {

        this.state = newState;
    }


    public void increaseHeadCount() {

        this.currentHeadCount++;
    }


    public boolean isFull() {

        return this.getCurrentHeadCount().equals(this.getMaxHeadCount());
    }


    public boolean hasEnded() {

        return this.getEndDate().isBefore(LocalDateTime.now());
    }

}
