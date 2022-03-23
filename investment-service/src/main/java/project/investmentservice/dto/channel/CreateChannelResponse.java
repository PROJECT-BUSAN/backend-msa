package project.investmentservice.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateChannelResponse {
    private String id;
    private Long channelNum;
    private String channelName;
}
