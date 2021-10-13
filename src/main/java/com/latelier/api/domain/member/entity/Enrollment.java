package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "ENROLLMENT_SEQ_GENERATOR",
        sequenceName = "ENROLLMENT_SEQ")
public class Enrollment {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ENROLLMENT_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "bigint")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", columnDefinition = "bigint")
    private Course course;


    private Enrollment(final Member member,
                       final Course course) {

        this.member = member;
        this.course = course;
    }


    public static Enrollment of(final Member member,
                                final Course course) {

        course.increaseHeadCount();
        return new Enrollment(member, course);
    }
}
