package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.investmentservice.api.ChannelApiController;

import java.util.List;

import static project.investmentservice.api.ChannelApiController.*;

@Getter
@AllArgsConstructor
public class StockGameEndMessage {

    List<StockResult> stockResults;
    List<GameResult> gameResults;
    private String message;
}
