package project.investmentservice.domain.dto;

import lombok.Getter;
import project.investmentservice.domain.User;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ServerMessage {

    public enum MessageType {
        RENEWAL, NOTICE, START, CLOSE
    };

    private MessageType type;
    private String channelId;
    private Map<Long, User> users = new HashMap<>();
    private String message;

    public ServerMessage(MessageType type, String channelId, Map<Long, User> users, String message) {
        this.type = type;
        this.channelId = channelId;
        this.users = users;
        this.message = message;
    }
}
