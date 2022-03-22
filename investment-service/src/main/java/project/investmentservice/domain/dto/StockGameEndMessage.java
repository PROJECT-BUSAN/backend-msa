package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.investmentservice.api.ChannelApiController;
import project.investmentservice.enums.SocketServerMessageType;

import java.util.List;

import static project.investmentservice.api.ChannelApiController.*;

/**
 * StockResult : 게임 진행에 사용된 기업의 이름, 시작날짜, 종료 날짜를 반환
 * GameResult : 유저간의 게임 결과 반환
 * StockResult 인스턴스 리스트와 GameResult 인스턴스 리스트와 게임 종료 메시지를 발행
 * ServerMessage 의 일종
 */
@Getter
public class StockGameEndMessage extends PublishMessage {
    List<StockResult> stockResults;
    List<GameResult> gameResults;

    public StockGameEndMessage(SocketServerMessageType type, List<StockResult> stockResults, List<GameResult> gameResults) {
        super(type);
        this.stockResults = stockResults;
        this.gameResults = gameResults;
    }
}
