
# 🎉 ChatSTOMP-Back: WebSocket 기반 실시간 채팅 애플리케이션

> Spring Boot와 WebSocket을 활용한 **실시간 채팅 서버** 프로젝트입니다. WebSocket(SockJS, STOMP)을 사용하여 빠르고 효율적인 메시징 환경을 제공합니다.


## 📌 주요 기능

- **실시간 채팅**: WebSocket을 이용한 빠른 메시징 서비스
- **입장/퇴장 알림**: 사용자가 채팅방에 입장하거나 나갈 때 실시간 알림
- **사용자 수 카운트**: 실시간 사용자 수 업데이트

## 🚀 기술 스택

- **백엔드**: ![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
- **WebSocket**: ![STOMP](https://img.shields.io/badge/STOMP-8C3B2C?style=for-the-badge&logo=javascript&logoColor=white) ![SockJS](https://img.shields.io/badge/SockJS-0A0A0A?style=for-the-badge&logo=javascript&logoColor=white)
- **의존성 관리**: ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

## 📡 WebSocket 엔드포인트

- **STOMP 엔드포인트**: `/chat`  
  클라이언트가 WebSocket 연결 시 사용하는 엔드포인트입니다. SockJS를 사용하여 WebSocket을 지원하지 않는 브라우저에서도 연결 가능하게 설정되었습니다.

## 📂 프로젝트 구조

```bash
├── src
│   ├── main
│   │   ├── java
│   │   │   └── websocket
│   │   │       ├── coupong
│   │   │       │   ├── message
│   │   │       │   │   └── ChatMessage.java
│   │   │       │   ├── websocketconfig
│   │   │       │   │   └── WebSocketConfig.java
│   │   │       │   ├── websocketcontroller
│   │   │       │   │   └── WebsocketController.java
│   ├── resources
│   │   └── application.properties
└── README.md
```

## 📖 주요 코드 설명

### `ChatMessage.java`

- 채팅 메시지를 위한 클래스. `sender`와 `content` 필드를 통해 메시지를 주고받습니다.

### `WebSocketConfig.java`

- STOMP를 이용해 메시지 브로커를 설정하고 SockJS로 WebSocket을 지원하지 않는 브라우저에서도 메시징이 가능하도록 설정합니다.

### `WebsocketController.java`

- 사용자가 채팅에 입장하거나 메시지를 전송할 때 이벤트를 처리하고, 모든 사용자에게 실시간으로 브로드캐스트합니다.

