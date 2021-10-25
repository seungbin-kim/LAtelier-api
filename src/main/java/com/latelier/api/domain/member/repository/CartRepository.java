package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.member.entity.Cart;
import com.latelier.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT ct " +
            "FROM Cart ct " +
            "JOIN FETCH ct.course c " +
            "WHERE ct.member = :member")
    List<Cart> findAllWithCourseByMember(@Param("member") final Member member);

    boolean existsByMemberAndCourse(final Member member, final Course course);

    @Modifying
    long deleteByCourseIdAndMember(final Long courseId, final Member member);

    @Modifying
    void deleteAllByMember(final Member member);

    @Modifying
    void deleteByCourseIn(final List<Course> courses);

}