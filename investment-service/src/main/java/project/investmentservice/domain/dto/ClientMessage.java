package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import project.investmentservice.enums.SocketClientMessageType;

/**
 * 클라이언트 측에서 소켓을 통해 서버로 전송하는 메시지 형식
 */
@Getter
@AllArgsConstructor
public class ClientMessage {

    private SocketClientMessageType type;
    private String channelId;
    private Long senderId;
    private String senderName;

}
