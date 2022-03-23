package project.investmentservice.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.investmentservice.domain.Channel;

import java.util.List;

@Data
@AllArgsConstructor
public class AllChannelResponse {
    private List<Channel> channels;
}
