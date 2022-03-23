package project.investmentservice.dto.channel;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateChannelRequest {
    @NotNull
    private String name;
    @NotNull
    private int limitOfParticipants;
    @NotNull
    private double entryFee;
    private Long userId;
    private String username;
}