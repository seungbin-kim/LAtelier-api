package com.latelier.api.domain.chat.entity;

import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "CHAT_ROOM_JOIN_SEQ_GENERATOR",
        sequenceName = "CHAT_ROOM_JOIN_SEQ")
public class ChatRoomJoin extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CHAT_ROOM_JOIN_SEQ")
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "bigint")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", columnDefinition = "bigint")
    private ChatRoom chatRoom;


    public ChatRoomJoin(final Member member,
                        final ChatRoom chatRoom) {

        this.member = member;
        this.chatRoom = chatRoom;
    }

}
