package project.investmentservice.dto.channel;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnterChannelRequest {
    @NotNull
    private Long userId;
    private String username;
}
