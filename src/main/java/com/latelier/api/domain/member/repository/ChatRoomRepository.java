package com.latelier.api.domain.member.repository;

import com.latelier.api.domain.member.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
