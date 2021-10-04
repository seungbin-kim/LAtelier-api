package com.latelier.api.domain.chat.entity;

import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "CHAT_MESSAGE_SEQ_GENERATOR",
        sequenceName = "CHAT_MESSAGE_SEQ")
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CHAT_MESSAGE_SEQ")
    private Long id;

    @Column(length = 500)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "bigint")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", columnDefinition = "bigint")
    private ChatRoom chatRoom;


    private ChatMessage(final String message,
                        final Member member,
                        final ChatRoom chatRoom) {

        this.message = message;
        this.member = member;
        this.chatRoom = chatRoom;
    }


    public static ChatMessage of(final String message,
                                 final Member member,
                                 final ChatRoom chatRoom) {

        return new ChatMessage(message, member, chatRoom);
    }

}
