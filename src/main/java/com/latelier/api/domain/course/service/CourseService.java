package com.latelier.api.domain.course.service;

import com.latelier.api.domain.course.entity.Category;
import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.entity.CourseCategory;
import com.latelier.api.domain.course.enumuration.CourseState;
import com.latelier.api.domain.course.exception.CourseNotFoundException;
import com.latelier.api.domain.course.packet.request.ReqCourseRegister;
import com.latelier.api.domain.course.packet.response.ResCourseDetails;
import com.latelier.api.domain.course.packet.response.ResCourseRegister;
import com.latelier.api.domain.course.packet.response.ResCourseSimple;
import com.latelier.api.domain.course.repository.CategoryRepository;
import com.latelier.api.domain.course.repository.CourseCategoryRepository;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.course.repository.CourseRepositoryCustom;
import com.latelier.api.domain.file.entity.CourseFile;
import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.file.enumuration.FileGroup;
import com.latelier.api.domain.file.repository.CourseFileRepository;
import com.latelier.api.domain.file.service.FileService;
import com.latelier.api.domain.member.entity.Enrollment;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.packet.response.ResMyCourse;
import com.latelier.api.domain.member.repository.EnrollmentRepository;
import com.latelier.api.domain.member.service.MemberService;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.latelier.api.domain.file.service.FileService.UploadRequest;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseFileRepository courseFileRepository;

    private final CategoryRepository categoryRepository;

    private final CourseCategoryRepository courseCategoryRepository;

    private final CourseRepository courseRepository;

    private final CourseRepositoryCustom courseRepositoryCustom;

    private final EnrollmentRepository enrollmentRepository;

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
    @Transactional(rollbackFor = Throwable.class)
    public ResCourseRegister addCourse(final Long instructorId,
                                       final ReqCourseRegister request) {

        Member instructor = memberService.getMemberById(instructorId);
        Course course = createCourse(request, instructor);
        List<Category> categories = setCategories(request.getCategoryIds(), course);
        courseRepository.save(course);

        List<File> files = uploadCourseFiles(request, course.getId());
        saveFilesInformation(course, files);

        return ResCourseRegister.of(
                course,
                categories,
                files);
    }


    /**
     * 강의목록을 검색합니다.
     *
     * @param state    강의 상태 문자열
     *                 com.latelier.api.domain.course.enumuration.CourseState
     * @param search   검색어
     * @param pageable 페이징, 정렬정보
     * @return 검색된 강의목록 페이지
     */
    public Page<ResCourseSimple> search(final String state,
                                        final String search,
                                        final Pageable pageable) {

        Page<Course> coursePage = courseRepositoryCustom.searchWithMember(state, search, pageable);
        Map<Course, File> courseImageFileMap = getCourseFileMap(coursePage.getContent());
        return coursePage.map(course -> ResCourseSimple.of(course, courseImageFileMap.get(course)));
    }


    /**
     * 강사의 강의목록 또는 수강 강의목록 조회
     *
     * @param memberId 회원 ID
     * @param pageable 페이징 정보
     * @return 강사의 강의목록 또는 수강생의 강의목록
     */
    public Page<ResMyCourse> getMyCourses(final Long memberId,
                                          final Pageable pageable,
                                          final boolean isTeaching) {

        Member member = memberService.getMemberById(memberId);
        Page<Course> coursePage;
        if (isTeaching) {
            coursePage = courseRepository.findByInstructor(member, pageable);
        } else {
            coursePage = enrollmentRepository.findByMember(member, pageable)
                    .map(Enrollment::getCourse);
        }
        Map<Course, File> courseImageFileMap = getCourseFileMap(coursePage.getContent());
        return coursePage.map(course -> isTeaching ?
                ResMyCourse.forInstructor(course, courseImageFileMap.get(course))
                : ResMyCourse.forMember(course, courseImageFileMap.get(course)));
    }


    /**
     * 강의 상세정보를 반환합니다.
     *
     * @param memberId 사용자 ID
     * @param courseId 강의 ID
     * @return 강의 상세정보
     */
    public ResCourseDetails getCourseDetails(@Nullable final Long memberId,
                                             final Long courseId) {

        Course course = getCourseWithInstructor(courseId);

        List<Category> categories = courseCategoryRepository.findAllWithCategoryByCourse(course).stream()
                .map(CourseCategory::getCategory)
                .collect(Collectors.toList());

        List<File> files = courseFileRepository.findAllWithFileByCourse(course).stream()
                .map(CourseFile::getFile)
                .collect(Collectors.toList());

        boolean hasPaid = memberId != null && enrollmentRepository.existsByMemberIdAndCourseId(memberId, course.getId());
        return ResCourseDetails.of(hasPaid, course, categories, files);
    }


    /**
     * WAITING 강의상태를 APPROVED 으로 변경합니다.
     *
     * @param courseId 허용할 강의 ID
     */
    @Transactional(rollbackFor = Throwable.class)
    public void approveCourse(final Long courseId) {

        Course course = getCourseWithoutGraph(courseId);

        switch (course.getState()) {
            case APPROVED:
                throw new BusinessException(ErrorCode.COURSE_STATE_ALREADY_APPROVED);
            case WAITING:
                course.changeState(CourseState.APPROVED);
        }
    }


    /**
     * 강의 ID에 대한 등록정보들을 조회합니다.
     *
     * @param memberId 강사 ID
     * @param courseId 강의 ID
     * @return 등록정보 리스트
     */
    public List<Enrollment> getEnrollmentList(final Long memberId, final Long courseId) {

        Member currentMember = memberService.getMemberById(memberId);
        Course course = getCourseWithInstructor(courseId);
        if (!course.getInstructor().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.COURSE_INSTRUCTOR_NOT_MATCH);
        }
        return enrollmentRepository.findByCourse(course);
    }


    private List<Category> setCategories(final List<Long> categoryIds,
                                         final Course course) {

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        categories.stream()
                .map(category -> CourseCategory.of(course, category))
                .forEach(courseCategory -> course.getCategories().add(courseCategory));
        return categories;
    }


    /**
     * 강의와 강의 파일들을 연결합니다.
     * 강의에 대한 파일정보들이 저장됩니다.
     *
     * @param course 강의
     * @param files  강의와 관련된 파일들
     */
    private void saveFilesInformation(final Course course,
                                      final List<File> files) {

        courseFileRepository.saveAll(
                files.stream()
                        .map(file -> CourseFile.of(course, file))
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
                request.getCoursePrice(),
                request.getHeadCount(),
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


    /**
     * 강의와 강의 썸네일 이미지를 매핑
     *
     * @param courses 강의들
     * @return <강의, 썸네일 파일> Map
     */
    private Map<Course, File> getCourseFileMap(final List<Course> courses) {

        List<CourseFile> courseFiles = courseFileRepository
                .findWithFileByFileGroupAndCourses(FileGroup.COURSE_THUMBNAIL_IMAGE, courses);
        Map<Course, File> courseImageFileMap = new HashMap<>();
        courseFiles.forEach(courseFile -> courseImageFileMap.put(courseFile.getCourse(), courseFile.getFile()));
        return courseImageFileMap;
    }


    private Course getCourseWithInstructor(final Long courseId) {

        return courseRepository.findWithInstructorById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_NOT_FOUND));
    }


    private Course getCourseWithoutGraph(final Long courseId) {

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

}