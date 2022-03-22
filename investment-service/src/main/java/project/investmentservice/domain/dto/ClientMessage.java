package project.investmentservice.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import project.investmentservice.enums.SocketClientMessageType;

/**
 * 클라이언트 측에서 소켓을 통해 서버로 전송하는 메시지 형식
 */
@Getter @Setter
public class ClientMessage {

    private SocketClientMessageType type;
    private String channelId;
    private Long senderId;
    private String senderName;

    public ClientMessage(SocketClientMessageType type, String channelId, Long senderId, String senderName) {
        this.type = type;
        this.channelId = channelId;
        this.senderId = senderId;
        this.senderName = senderName;
    }
}
