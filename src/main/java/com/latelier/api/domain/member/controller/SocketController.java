package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.packet.response.ResChatMessage;
import com.latelier.api.domain.member.service.ChatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate template;

    private final ChatService chatService;


    @MessageMapping("/chat/app")
    public void sendMessage(final ChatMessage chatMessage) {

        ResChatMessage resChatMessage = chatService.saveChatMessage(chatMessage);
        // 주고받는 방법 더 생각하기
        template.convertAndSend("/topic/" + chatMessage.getChatRoomId(), resChatMessage);
    }


    @Getter
    public static class ChatMessage {

        private Long chatRoomId;

        private Long senderId;

        private Long receiverId;

        private String message;

    }

}
