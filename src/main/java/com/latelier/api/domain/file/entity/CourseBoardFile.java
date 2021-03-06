package com.latelier.api.domain.file.entity;

import com.latelier.api.domain.course.entity.CourseBoard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "COURSE_BOARD_FILE_SEQ_GENERATOR",
        sequenceName = "COURSE_BOARD_FILE_SEQ")
public class CourseBoardFile {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COURSE_BOARD_FILE_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_board_id", columnDefinition = "bigint", nullable = false)
    private CourseBoard board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", columnDefinition = "bigint", nullable = false)
    private File file;

}
