package com.latelier.api.domain.course.service;

import com.latelier.api.domain.course.entity.Category;
import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.CourseCategory;
import com.latelier.api.domain.course.packet.request.ReqCourseRegister;
import com.latelier.api.domain.course.packet.response.ResCourseRegister;
import com.latelier.api.domain.course.repository.CategoryRepository;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.file.entity.CourseFile;
import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.file.enumuration.FileGroup;
import com.latelier.api.domain.file.repository.CourseFileRepository;
import com.latelier.api.domain.file.service.FileService;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.latelier.api.domain.file.service.FileService.UploadRequest;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseFileRepository courseFileRepository;

    private final CategoryRepository categoryRepository;

    private final CourseRepository courseRepository;

    private final MemberService memberService;

    private final FileService fileService;


    /**
     * 강의를 등록합니다.
     * 업로드한 파일도 모두 업로드한 뒤 정보를 저장하고, 등록된 결과를 리턴합니다.
     *
     * @param instructorId ID
     * @param request      강의등록 요청
     * @return 등록 결과
     */
    @Transactional
    public ResCourseRegister addCourse(final Long instructorId,
                                       final ReqCourseRegister request) {

        Member instructor = memberService.getMemberById(instructorId);
        Course course = createCourse(request, instructor);
        List<Category> categories = setCategories(request.getCategoryIds(), course);
        courseRepository.save(course);

        List<File> files = uploadCourseFiles(request, course.getId());
        connectCourseToFiles(course, files);

        return ResCourseRegister.of(
                course,
                categories,
                files);
    }


    private List<Category> setCategories(final List<Long> categoryIds,
                                         final Course course) {

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        categories.stream()
                .map(category -> CourseCategory.of(course, category))
                .forEach(courseCategory -> course.getCourseCategories().add(courseCategory));
        return categories;
    }


    /**
     * 강의와 강의 파일들을 연결합니다.
     * 강의에 대한 파일정보들이 저장됩니다.
     *
     * @param course 강의
     * @param files  강의와 관련된 파일들
     */
    private void connectCourseToFiles(final Course course,
                                      final List<File> files) {

        courseFileRepository.saveAll(
                files.stream()
                        .map(file -> CourseFile.createAndConnect(course, file))
                        .collect(Collectors.toList()));
    }


    /**
     * 강의 엔티티를 만들어서 반환합니다.
     *
     * @param request    강의등록 요청
     * @param instructor 강사 Entity
     * @return 강의 Entity
     */
    private Course createCourse(final ReqCourseRegister request,
                                final Member instructor) {

        return Course.of(
                instructor,
                request.getCourseName(),
                request.getExplanation(),
                request.getPrice(),
                request.getMaxSize(),
                request.getStartDate(),
                request.getEndDate());
    }


    /**
     * 강의 등록에 필요한 파일들을 S3 서버에 업로드합니다.
     * 파일들은 courses/{courseId}/ 폴더에 업로드 됩니다.
     *
     * @param request  강의등록 요청
     * @param courseId 강의 ID
     * @return 업로드 된 파일정보 Entity
     */
    private List<File> uploadCourseFiles(final ReqCourseRegister request,
                                         final Long courseId) {

        return fileService.uploadFile(false, courseId, FileGroup.Path.COURSE.getPath(),
                new UploadRequest(request.getProofImageFile(), FileGroup.COURSE_PROOF_IMAGE),
                new UploadRequest(request.getThumbnailImageFile(), FileGroup.COURSE_THUMBNAIL_IMAGE),
                new UploadRequest(request.getDetailImageFile(), FileGroup.COURSE_DETAIL_IMAGE),
                new UploadRequest(request.getVideoFile(), FileGroup.COURSE_VIDEO));
    }


}
