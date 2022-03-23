package project.investmentservice.dto;

import lombok.*;
import project.investmentservice.domain.User;
import project.investmentservice.enums.ClientMessageType;
import project.investmentservice.enums.ServerMessageType;

import java.util.List;
import java.util.Map;

@Getter
public class SocketDto {

    @Getter
    @AllArgsConstructor
    public static class StockResult {
        private Long companyId;
        private String stockName;
        private String startDate;
        private String endDate;
        private String message;
    }

    /**
     * 10초에 한 번씩 주가 정보 전송에 쓰이는 객체
     */
    @Getter
    public static class StockInfoMessage extends PublishMessage {

        private String date;
        private double close;
        private double open;
        private double high;
        private double low;
        private int volume;
        private Long company_id;

        public StockInfoMessage(ServerMessageType type, String date, double close, double open, double high, double low, int volume, Long company_id) {
            super(type);
            this.date = date;
            this.close = close;
            this.open = open;
            this.high = high;
            this.low = low;
            this.volume = volume;
            this.company_id = company_id;
        }
    }

    @Getter
    public static class StockGameEndMessage extends PublishMessage {
        List<StockResult> stockResults;
        List<GameResult> gameResults;

        public StockGameEndMessage(ServerMessageType type, List<StockResult> stockResults, List<GameResult> gameResults) {
            super(type);
            this.stockResults = stockResults;
            this.gameResults = gameResults;
        }
    }

    @Getter @Setter
    public static class ServerMessage extends PublishMessage {


        private String channelId;
        private Map<Long, User> users;

        public ServerMessage(ServerMessageType type, String channelId, Map<Long, User> users) {
            super(type);
            this.channelId = channelId;
            this.users = users;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PublishMessage {
        private ServerMessageType type;
    }

    /**
     * 유저간의 게임 결과 반환
     * 유저의 이율 기준 내림차순
     */
    @Getter
    @AllArgsConstructor
    public static class GameResult {
        private String userName;
        private Long userId;
        private double userProfit;
        private double userProfitRate;
    }

    /**
     * 클라이언트 측에서 소켓을 통해 서버로 전송하는 메시지 형식
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClientMessage {
        private ClientMessageType type;
        private String channelId;
        private Long senderId;
        private String senderName;
    }

    @Data
    public static class AllUserPointDeduction{
        private List<Long> userIds;
        private double fee;

        public AllUserPointDeduction(List<Long> userIds, double fee) {
            this.userIds = userIds;
            this.fee = fee;
        }
    }
}
