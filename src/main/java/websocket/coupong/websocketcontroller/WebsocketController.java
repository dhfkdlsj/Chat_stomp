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
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    private void updateUserCount() {
        // 사용자 수를 계산하고 브로드캐스트
        int userCount = sessionUserMap.size();
        messagingTemplate.convertAndSend("/sub/userCount", userCount);
    }

    @MessageMapping("/join")
    public void join(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String username = message.getSender();

        String sessionId = headerAccessor.getSessionId();
        sessionUserMap.put(sessionId, username);

        // 모든 사용자에게 입장 메시지 브로드캐스트
        ChatMessage broadcastMessage = new ChatMessage(username, " 님이 채팅방에 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat", broadcastMessage);

        // 사용자 수 업데이트
        updateUserCount();
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/sub/chat", message);
    }

    @MessageMapping("/leave")
    public void leave(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String username = sessionUserMap.remove(sessionId);

        if (username != null) {
            // 퇴장 메시지 브로드캐스트
            ChatMessage leaveMessage = new ChatMessage(username, " 님이 채팅방에서 나가셨습니다.");
            messagingTemplate.convertAndSend("/sub/chat", leaveMessage);

            // 사용자 수 업데이트
            updateUserCount();
        }
    }
}