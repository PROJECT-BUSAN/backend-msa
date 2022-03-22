package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.investmentservice.domain.User;
import project.investmentservice.enums.SocketServerMessageType;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ServerMessage extends PublishMessage {

    
    private String channelId;
    private Map<Long, User> users = new HashMap<>();

    public ServerMessage(SocketServerMessageType type, String channelId, Map<Long, User> users) {
        super(type);
        this.channelId = channelId;
        this.users = users;
    }
}
