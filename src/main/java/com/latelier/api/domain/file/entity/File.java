package com.latelier.api.domain.file.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "FILE_SEQ_GENERATOR",
        sequenceName = "FILE_SEQ")
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "FILE_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", columnDefinition = "bigint")
    private FileGroup group;

    @Column(length = 50)
    private String originalFileName;

    @Column(length = 100)
    private String storedFileName;

    @Column(length = 1000)
    private String uri;

    @Column(columnDefinition = "bigint")
    private Long fileSize;

}
