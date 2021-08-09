package com.latelier.api.domain.file.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "FILE_GROUP_SEQ_GENERATOR",
        sequenceName = "FILE_GROUP_SEQ")
public class FileGroup {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "FILE_GROUP_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(length = 20)
    private String file_group_name;

    @Column(length = 50)
    private String path;

}
