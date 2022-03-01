package project.investmentservice.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class ClientMessage {

    public enum MessageType {
        ENTER, EXIT, READY, CANCEL
    };

    private MessageType type;
    private String channelId;
    private Long senderId;

    public ClientMessage(MessageType type, String channelId, Long senderId) {
        this.type = type;
        this.channelId = channelId;
        this.senderId = senderId;
    }
}
