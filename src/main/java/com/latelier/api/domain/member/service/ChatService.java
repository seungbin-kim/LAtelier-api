package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.controller.SocketController;
import com.latelier.api.domain.member.entity.ChatMessage;
import com.latelier.api.domain.member.entity.ChatRoom;
import com.latelier.api.domain.member.entity.ChatRoomJoin;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.exception.ChatRoomNotFound;
import com.latelier.api.domain.member.packet.response.ResChatMessage;
import com.latelier.api.domain.member.packet.response.ResChatRoom;
import com.latelier.api.domain.member.repository.ChatMessageRepository;
import com.latelier.api.domain.member.repository.ChatRoomJoinRepository;
import com.latelier.api.domain.member.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final MemberService memberService;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomJoinRepository chatRoomJoinRepository;


    @Transactional
    public ResChatRoom addChatRoom(final Long senderId, final Long receiverId) {

        Member sender = memberService.getMemberById(senderId);
        Member receiver = memberService.getMemberById(receiverId);

        return chatRoomJoinRepository.findAlreadyOpenChat(senderId, receiverId)
                .map(chatRoomJoin ->
                        ResChatRoom.createResponse(chatRoomJoin, sender.getName(), receiver.getName()))
                .orElseGet(() -> createRoomAndJoin(sender, receiver));
    }


    //    @Transactional
    public List<ResChatMessage> getChatMessages(final Long roomId) {

        // TODO 사용자 확인

        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ResChatMessage::createResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public ResChatMessage saveChatMessage(final SocketController.ChatMessage chatMessage) {

        Member sender = memberService.getMemberById(chatMessage.getSenderId());
        Long chatRoomId = chatMessage.getChatRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFound(String.valueOf(chatRoomId)));

        ChatMessage newMessage = new ChatMessage(chatMessage.getMessage(), sender, chatRoom);
        ChatMessage savedMessage = chatMessageRepository.save(newMessage);
        return ResChatMessage.createResponse(savedMessage);
    }


    private ResChatRoom createRoomAndJoin(final Member sender, final Member receiver) {

        ChatRoom newChatRoom = chatRoomRepository.save(new ChatRoom());

        chatRoomJoinRepository.save(new ChatRoomJoin(sender, newChatRoom));
        chatRoomJoinRepository.save(new ChatRoomJoin(receiver, newChatRoom));

        return ResChatRoom.createResponse(newChatRoom, sender.getName(), receiver.getName());
    }

}
