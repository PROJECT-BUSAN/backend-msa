package project.investmentservice.domain.dto;

import lombok.Getter;
import project.investmentservice.api.ChannelApiController;

import java.util.List;

@Getter
public class StockGameEndMessage {

    List<ChannelApiController.gameEndResponse> responseList;
    private String message;

    public StockGameEndMessage(List<ChannelApiController.gameEndResponse> responseList, String message) {
        this.responseList = responseList;
        this.message = message;
    }
}
