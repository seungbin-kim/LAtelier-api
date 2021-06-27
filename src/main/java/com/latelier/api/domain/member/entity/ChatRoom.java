package com.latelier.api.domain.member.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@SequenceGenerator(
        name = "CHAT_ROOM_SEQ_GENERATOR",
        sequenceName = "CHAT_ROOM_SEQ",
        allocationSize = 50)
public class ChatRoom {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CHAT_ROOM_SEQ")
    @Column(columnDefinition = "bigint")
    private Long id;

}
