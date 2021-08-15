package com.latelier.api.domain.file.entity;

import com.latelier.api.domain.file.enumuration.FileGroup;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
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

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private FileGroup fileGroup;

    @Column(length = 50)
    private String originalFilename;

    @Column(length = 100)
    private String storedFilename;

    @Column(length = 1000)
    private String uri;

    @Column(columnDefinition = "bigint")
    private Long fileSize;


    private File(final FileGroup fileGroup,
                 final String originalFilename,
                 final String storedFilename,
                 final String uri,
                 final Long fileSize) {

        this.fileGroup = fileGroup;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.uri = uri;
        this.fileSize = fileSize;
    }


    public static File of(final FileGroup fileGroup,
                          final String originalFilename,
                          final String storedFilename,
                          final String uri,
                          final Long fileSize) {

        return new File(
                fileGroup,
                originalFilename,
                storedFilename,
                uri,
                fileSize);
    }

}
