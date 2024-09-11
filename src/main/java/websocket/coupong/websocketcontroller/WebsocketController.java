package websocket.coupong.websocketcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import websocket.coupong.message.ChatMessage;


import java.util.HashMap;
import java.util.Map;

@Controller
public class WebsocketController {

    // 유저 세션 관리
    private final Map<String, String> sessionUserMap = new HashMap<>();

    // SimpMessagingTemplate을 사용하여 특정 사용자에게 메시지를 보냄
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 클라이언트에서 "/app/join" 경로로 메시지를 보내면 처리
    @MessageMapping("/join")
    public void join(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String username = message.getSender();

        // 세션에 사용자 이름 저장
        String sessionId = headerAccessor.getSessionId();
        sessionUserMap.put(sessionId, username);

        // 모든 사용자에게 입장 메시지 브로드캐스트
        ChatMessage broadcastMessage = new ChatMessage(username, " 님이 채팅방에 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat", broadcastMessage);
    }

    // 클라이언트에서 "/app/chat" 경로로 메시지를 보내면 처리
    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        // 받은 메시지를 모든 클라이언트에게 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat", message);
    }

    // 클라이언트에서 "/app/leave" 경로로 메시지를 보내면 처리
    @MessageMapping("/leave")
    public void leave(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String username = sessionUserMap.remove(sessionId);

        if (username != null) {
            // 퇴장 메시지 브로드캐스트
            ChatMessage leaveMessage = new ChatMessage(username, " 님이 채팅방에서 나가셨습니다.");
            messagingTemplate.convertAndSend("/sub/chat", leaveMessage);
        }
    }
}