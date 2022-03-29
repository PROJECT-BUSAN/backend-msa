package project.investmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import project.investmentservice.domain.Channel;
import project.investmentservice.enums.HttpReturnType;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ChannelDto {

    @Data
    @AllArgsConstructor
    public static class EnterChannelResponse {
        private HttpReturnType type;
        private String message;
        private Long userId;
        private String username;
    }

    @Data
    public static class EnterChannelRequest {
        @NotNull
        private Long userId;
        private String username;
    }

    @Data
    @AllArgsConstructor
    public static class CreateChannelResponse {
        private String id;
        private Long channelNum;
        private String channelName;
        private Long hostId;
    }

    @Data
    public static class CreateChannelRequest {
        @NotNull
        private String name;
        @NotNull
        private int limitOfParticipants;
        @NotNull
        private double entryFee;
        private Long userId;
        private String username;
    }

    @Data
    @AllArgsConstructor
    public static class AllChannelResponse {
        private List<Channel> channels;
    }
}
