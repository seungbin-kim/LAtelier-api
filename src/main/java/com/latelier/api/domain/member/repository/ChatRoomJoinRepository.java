package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {

    @Query("SELECT chj " +
            "FROM ChatRoomJoin chj " +
            "JOIN FETCH chj.member " +
            "WHERE EXISTS (SELECT chj FROM ChatRoomJoin chj WHERE chj.member = :memberIdOne) " +
            "AND EXISTS (SELECT chj FROM ChatRoomJoin chj WHERE chj.member = :memberIdTwo)")
    Optional<ChatRoomJoin> findAlreadyOpenChat(@Param("memberIdOne") Long memberIdOne, @Param("memberIdTwo") Long memberIdTwo);

}
