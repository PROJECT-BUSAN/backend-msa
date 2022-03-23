package project.investmentservice.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.investmentservice.enums.HttpReturnType;

@Data
@AllArgsConstructor
public class EnterChannelResponse {
    private HttpReturnType type;
    private String message;
    private Long userId;
    private String username;
}
