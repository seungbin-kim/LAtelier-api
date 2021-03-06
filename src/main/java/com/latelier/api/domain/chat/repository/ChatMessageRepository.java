package com.latelier.api.domain.chat.repository;

import com.latelier.api.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @EntityGraph(attributePaths = {"member", "chatRoom"})
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long roomId);

}
