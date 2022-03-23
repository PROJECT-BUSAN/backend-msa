package project.investmentservice.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.investmentservice.enums.SocketServerMessageType;

@Getter
@AllArgsConstructor
public class PublishMessage {
    private SocketServerMessageType type;
}
