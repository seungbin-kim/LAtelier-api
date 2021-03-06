package com.latelier.api.domain.chat.repository;

import com.latelier.api.domain.chat.entity.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {

    @Query("SELECT chj1 " +
            "FROM ChatRoomJoin chj1, ChatRoomJoin chj2 " +
            "JOIN FETCH chj1.member " +
            "WHERE chj1.member.id = :memberIdOne " +
            "AND chj2.member.id = :memberIdTwo")
    Optional<ChatRoomJoin> findAlreadyOpenedChat(@Param("memberIdOne") Long memberIdOne, @Param("memberIdTwo") Long memberIdTwo);

}
