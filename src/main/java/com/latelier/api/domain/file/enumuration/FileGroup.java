package com.latelier.api.domain.file.enumuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileGroup {

    PROFILE_IMAGE("프로필 이미지"),

    COURSE_PROOF_IMAGE("강의 결과물 이미지"),
    COURSE_THUMBNAIL_IMAGE("강의 썸네일 이미지"),
    COURSE_DETAIL_IMAGE("강의 상세정보 이미지"),
    COURSE_VIDEO("강의 비디오");

    private final String description;

    @Getter
    @RequiredArgsConstructor
    public enum Path {

        PROFILE("profile/"),
        COURSE("courses/");

        private final String path;

    }

}