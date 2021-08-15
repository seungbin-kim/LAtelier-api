package com.latelier.api.domain.file.entity;

import com.latelier.api.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "COURSE_FILE_SEQ_GENERATOR",
        sequenceName = "COURSE_FILE_SEQ")
public class CourseFile {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COURSE_FILE_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", columnDefinition = "bigint", nullable = false)
    private Course course;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fild_id", columnDefinition = "bigint", nullable = false)
    private File file;


    private CourseFile(final Course course,
                       final File file) {

        this.course = course;
        this.file = file;
    }


    public static CourseFile createAndConnect(final Course course,
                                              final File file) {

        CourseFile courseFile = new CourseFile(course, file);
        course.getCourseFiles().add(courseFile);
        return courseFile;
    }

}
