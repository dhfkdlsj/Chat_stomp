package websocket.coupong.message;

public class ChatMessage {
    private String sender;
    private String content;

    // 기본 생성자
    public ChatMessage() {}

    // 생성자
    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    // getter and setter
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}