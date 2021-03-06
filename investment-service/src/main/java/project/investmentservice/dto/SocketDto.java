package project.investmentservice.dto;

import lombok.*;
import project.investmentservice.domain.User;
import project.investmentservice.enums.ClientMessageType;
import project.investmentservice.enums.ServerMessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class SocketDto {

    @Getter
    @AllArgsConstructor
    public static class StockResult {
        private Long companyId;
        private String stockName;
        private String startDate;
        private String endDate;
    }

    @Getter
    @NoArgsConstructor
    public static class StockInfoMessages extends PublishMessage {
        private List<StockInfoMessage> stockInfoMessageList;

        public StockInfoMessages(ServerMessageType type, String channelId) {
            super(type, channelId);
            stockInfoMessageList = new ArrayList<>();
        }

        public void addStockInfoMessage(StockInfoMessage sm) {
            stockInfoMessageList.add(sm);
        }
    }



    /**
     * 10초에 한 번씩 주가 정보 전송에 쓰이는 객체
     */
    @Getter
    @NoArgsConstructor
    public static class StockInfoMessage extends PublishMessage {

        private String date;
        private double close;
        private double open;
        private double high;
        private double low;
        private int volume;
        private Long companyId;

        public StockInfoMessage(ServerMessageType type, String channelId, String date, double close, double open, double high, double low, int volume, Long companyId) {
            super(type, channelId);
            this.date = date;
            this.close = close;
            this.open = open;
            this.high = high;
            this.low = low;
            this.volume = volume;
            this.companyId = companyId;
        }
    }

    @Getter
    public static class StockGameEndMessage extends PublishMessage {
        List<StockResult> stockResults;
        List<GameResult> gameResults;

        public StockGameEndMessage(ServerMessageType type, String channelId, List<StockResult> stockResults, List<GameResult> gameResults) {
            super(type, channelId);
            this.stockResults = stockResults;
            this.gameResults = gameResults;
        }
    }

    @Getter @Setter
    public static class ServerMessage extends PublishMessage {

        private Map<Long, User> users;

        public ServerMessage(ServerMessageType type, String channelId, Map<Long, User> users) {
            super(type, channelId);
            this.users = users;
        }
    }

    @Getter
    public static class GameInitMessage extends PublishMessage {
        private List<Long> companyIds;

        public GameInitMessage(ServerMessageType type, String channelId, Set<Long> companyIds) {
            super(type, channelId);
            this.companyIds = new ArrayList<>(companyIds);
        }
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublishMessage {
        private ServerMessageType type;
        private String channelId;
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
    @NoArgsConstructor
    public static class AllUserPointUpdate {
        private List<Long> userIds;
        private List<Double> fees;

        public AllUserPointUpdate(List<Long> userIds, List<Double> fees) {
            this.userIds = userIds;
            this.fees = fees;
        }
    }
}
