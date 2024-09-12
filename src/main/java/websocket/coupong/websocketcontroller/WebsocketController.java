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

    // 사용자 세션과 사용자 이름을 매핑하는 Map
    private final Map<String, String> sessionUserMap = new HashMap<>();

    // 메시지를 브로드캐스트하는 템플릿
    private final SimpMessagingTemplate messagingTemplate;

    // 의존성 주입을 통해 SimpMessagingTemplate 객체를 주입받음
    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 현재 채팅방의 사용자 수를 계산하고 브로드캐스트하는 메소드
    private void updateUserCount() {
        int userCount = sessionUserMap.size();
        // 사용자 수를 "/sub/userCount" 구독 주소로 전송
        messagingTemplate.convertAndSend("/sub/userCount", userCount);
    }

    // 사용자가 채팅방에 입장할 때 호출되는 메소드
    @MessageMapping("/join")
    public void join(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // 메시지에서 사용자 이름 추출
        String username = message.getSender();

        // 현재 세션 ID를 가져와서 세션과 사용자 이름을 매핑
        String sessionId = headerAccessor.getSessionId();
        sessionUserMap.put(sessionId, username);

        // 채팅방에 입장했다는 메시지를 생성하여 모든 사용자에게 브로드캐스트
        ChatMessage broadcastMessage = new ChatMessage(username, " 님이 채팅방에 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat", broadcastMessage);

        // 사용자 수 업데이트
        updateUserCount();
    }

    // 사용자가 메시지를 전송할 때 호출되는 메소드
    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        // 메시지를 "/sub/chat" 구독 주소로 전송하여 모든 사용자에게 전달
        messagingTemplate.convertAndSend("/sub/chat", message);
    }

    // 사용자가 채팅방을 떠날 때 호출되는 메소드
    @MessageMapping("/leave")
    public void leave(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // 현재 세션 ID를 가져와서 사용자 이름을 제거
        String sessionId = headerAccessor.getSessionId();
        String username = sessionUserMap.remove(sessionId);

        if (username != null) {
            // 채팅방에서 나갔다는 메시지를 생성하여 모든 사용자에게 브로드캐스트
            ChatMessage leaveMessage = new ChatMessage(username, " 님이 채팅방에서 나가셨습니다.");
            messagingTemplate.convertAndSend("/sub/chat", leaveMessage);

            // 사용자 수 업데이트
            updateUserCount();
        }
    }
}