package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.packet.request.ReqChatRoom;
import com.latelier.api.domain.member.packet.response.ResChatMessage;
import com.latelier.api.domain.member.packet.response.ResChatRoom;
import com.latelier.api.domain.member.service.ChatService;
import com.latelier.api.domain.model.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;


    //    @PreAuthorize("hasRole('USER')")
    @PostMapping("/rooms")
    @ApiOperation(
            value = "1:1 채팅방정보 생성",
            notes = "1:1 채팅방의 정보를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "403", description = "유저가 아님")})
    public ResponseEntity<Result<ResChatRoom>> createChatRoom(@RequestBody final ReqChatRoom reqChatRoom) {

        // SecurityUtil 사용?
        ResChatRoom room = chatService.addChatRoom(reqChatRoom.getSenderId(), reqChatRoom.getReceiverId());
        return ResponseEntity.ok(Result.of(room));
    }

    //    @PreAuthorize("hasRole('USER')")
    @GetMapping("/rooms/{roomId}/messages")
    @ApiOperation(
            value = "채팅방 메세지 반환",
            notes = "채팅방의 기존 메세지들을 반환합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "채팅방 ID", required = true, dataTypeClass = Long.class, paramType = "path")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메세지 목록 반환 성공"),
            @ApiResponse(responseCode = "403", description = "유저가 아님")})
    public ResponseEntity<Result<List<ResChatMessage>>> getChatMessages(@PathVariable("roomId") final Long roomId) {

        List<ResChatMessage> chatMessages = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(Result.of(chatMessages));
    }

}
