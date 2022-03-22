package project.investmentservice.domain.dto;

import lombok.Getter;
import project.investmentservice.domain.User;
import project.investmentservice.enums.SocketServerMessageType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ServerMessage {
    
    private SocketServerMessageType type;
    private String channelId;
    private Map<Long, User> users = new HashMap<>();
    private String message;

    public ServerMessage(SocketServerMessageType type, String channelId, Map<Long, User> users, String message) {
        this.type = type;
        this.channelId = channelId;
        this.users = users;
        this.message = message;
    }
}
