package com.latelier.api.domain.course.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.util.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.latelier.api.domain.course.entity.QCourse.course;
import static com.latelier.api.domain.member.entity.QMember.member;

@Repository
public class CourseRepositoryCustom extends Querydsl4RepositorySupport {

    public CourseRepositoryCustom() {
        super(Course.class);
    }


    public Page<Course> searchWithMember(final String state, final Pageable pageable) {

        return applyPagination(pageable, query -> query
                        .selectFrom(course)
                        .join(course.instructor, member).fetchJoin()
                        .where(stateLike(state)),
                countQuery -> countQuery
                        .select(course.id)
                        .from(course)
                        .where(stateLike(state)));
    }


    private BooleanExpression stateLike(final String state) {

        return StringUtils.hasText(state) ? course.state.stringValue().like(state) : null;
    }

}
