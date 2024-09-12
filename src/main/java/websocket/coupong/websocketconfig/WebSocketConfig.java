package websocket.coupong.websocketconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 및 클라이언트와의 메시징 경로 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에게 브로드캐스트할 메시지 경로 설정
        config.enableSimpleBroker("/sub");

        // 클라이언트에서 서버로 메시지를 전송할 경로 설정
        config.setApplicationDestinationPrefixes("/pub");
    }

    // STOMP 엔드포인트 및 허용된 출처 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP 엔드포인트를 "/chat"으로 설정
        registry.addEndpoint("/chat")
                // 허용된 출처를 로컬 개발 서버와 Netlify 도메인으로 설정
                .setAllowedOrigins("http://localhost:3000", "https://chatstomp.netlify.app")
                // SockJS를 사용하여 웹소켓 지원이 없는 브라우저에서도 사용할 수 있도록 설정
                .withSockJS();
    }
}