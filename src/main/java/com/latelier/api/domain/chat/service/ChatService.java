package com.latelier.api.domain.chat.service;

import com.latelier.api.controller.ChatController;
import com.latelier.api.domain.chat.entity.ChatMessage;
import com.latelier.api.domain.chat.entity.ChatRoom;
import com.latelier.api.domain.chat.entity.ChatRoomJoin;
import com.latelier.api.domain.chat.repository.ChatMessageRepository;
import com.latelier.api.domain.chat.repository.ChatRoomJoinRepository;
import com.latelier.api.domain.chat.repository.ChatRoomRepository;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.packet.response.ResChatMessage;
import com.latelier.api.domain.member.packet.response.ResChatRoom;
import com.latelier.api.domain.member.service.MemberService;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
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


    /**
     * 채팅방 추가
     *
     * @param senderId   보내는 사람 ID
     * @param receiverId 받는 사람 ID
     * @return 생성된 채팅방
     */
    @Transactional
    public ResChatRoom addChatRoom(final Long senderId,
                                   final Long receiverId) {

        Member sender = memberService.getMemberById(senderId);
        Member receiver = memberService.getMemberById(receiverId);

        return chatRoomJoinRepository.findAlreadyOpenedChat(senderId, receiverId)
                .map(chatRoomJoin ->
                        ResChatRoom.of(chatRoomJoin, sender.getUsername(), receiver.getUsername()))
                .orElseGet(() -> createRoomAndJoin(sender, receiver));
    }


    /**
     * 채팅기록 얻기
     *
     * @param roomId 채팅방 ID
     * @return 채팅방 ID 에 해당하는 채팅기록
     */
    public List<ResChatMessage> getChatMessages(final Long roomId) {

        // TODO 사용자 확인

        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ResChatMessage::of)
                .collect(Collectors.toList());
    }


    /**
     * 채팅기록 저장
     *
     * @param chatMessage 채팅 메세지
     * @return 저장된 채팅 메세지
     */
    @Transactional
    public ResChatMessage saveChatMessage(final ChatController.ChatMessage chatMessage) {

        Member sender = memberService.getMemberById(chatMessage.getSenderId());
        Long chatRoomId = chatMessage.getChatRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        ChatMessage newMessage = ChatMessage.of(chatMessage.getMessage(), sender, chatRoom);
        ChatMessage savedMessage = chatMessageRepository.save(newMessage);
        return ResChatMessage.of(savedMessage);
    }


    /**
     * 방 생성하고 입장하기
     *
     * @param sender   보내는 사람
     * @param receiver 받는 사람
     * @return 생성된 채팅방 정보
     */
    private ResChatRoom createRoomAndJoin(final Member sender,
                                          final Member receiver) {

        ChatRoom newChatRoom = chatRoomRepository.save(new ChatRoom());

        chatRoomJoinRepository.save(new ChatRoomJoin(sender, newChatRoom));
        chatRoomJoinRepository.save(new ChatRoomJoin(receiver, newChatRoom));

        return ResChatRoom.of(newChatRoom, sender.getUsername(), receiver.getUsername());
    }

}
