package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.service.ChatService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate template;

    private final ChatService chatService;


    @MessageMapping("/chat")
    public void sendMessage(final ChatMessage chatMessage) {

        chatService.saveChatMessage(chatMessage);
        template.convertAndSend("/topic/" + chatMessage.getReceiverId(), chatMessage);
    }

    @Getter
    @EqualsAndHashCode
    @ApiModel("채팅 메세지")
    public static class ChatMessage {

        @NotBlank(message = "채팅방 ID는 필수입니다.")
        @ApiModelProperty(
                value = "채팅방 ID",
                name = "chatRoomId",
                example = "3",
                required = true)
        private Long chatRoomId;

        @NotBlank(message = "보내는 유저의 ID는 필수입니다.")
        @ApiModelProperty(
                value = "메세지를 보내는 유저의 ID",
                name = "senderId",
                example = "1",
                required = true)
        private Long senderId;

        @NotBlank(message = "받는 유저의 ID는 필수입니다.")
        @ApiModelProperty(
                value = "메세지를 받는 유저의 ID",
                name = "receiver",
                example = "2",
                required = true)
        private Long receiverId;

        @Size(
                max = 500,
                message = "최대 500글자 입니다.")
        @ApiModelProperty(
                value = "메세지 내용",
                name = "message",
                example = "안녕하세요.")
        private String message;


        public void setMessage(String message) {
            this.message = message;
        }

    }

}
