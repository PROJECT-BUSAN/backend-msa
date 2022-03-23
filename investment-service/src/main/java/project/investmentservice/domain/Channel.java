package project.investmentservice.domain;

import lombok.Getter;
import lombok.Setter;
import project.investmentservice.dto.SocketDto.GameResult;
import project.investmentservice.enums.UserReadyType;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Long channelNum;
    private String channelName;
    private int LimitOfParticipants;
    private double entryFee;    // 참가비
    private Map<Long, User> users = new HashMap<>();
    private double pointPsum;
    private Long hostId;
    private String hostName;

    public static Channel create(String channelName, Long channelNum, int LimitOfParticipants, double entryFee, Long hostId, String hostname) {
        // 채널 정보 생성 + host 추가
        Channel channel = new Channel();
        channel.id = UUID.randomUUID().toString();
        channel.channelName = channelName;
        channel.channelNum = channelNum;
        channel.LimitOfParticipants = LimitOfParticipants;
        channel.entryFee = entryFee;
        channel.hostId = hostId;
        channel.hostName = hostname;

        User user = new User(entryFee, hostname);
        user.setReadyType(UserReadyType.READY);
        channel.users.put(hostId, user);
        channel.pointPsum = entryFee;
        return channel;
    }

    public List<GameResult> gameResult() {
        List<GameResult> results = new ArrayList<>();
        
        for(Long userKey : users.keySet()) {
            User user = users.get(userKey);
            double userProfitRate =  user.getSeedMoney()/entryFee;
            GameResult gameResult = new GameResult(
                    user.getName(),
                    userKey,
                    user.getSeedMoney(),
                    userProfitRate
            );
            results.add(gameResult);
        }
        results.stream().sorted(Comparator.comparing(GameResult::getUserProfitRate).reversed()).collect(Collectors.toList());

        return results;
    }

    public List<Long> getAllUsers() {
        List<Long> results = new ArrayList<>();

        for(Long userKey : users.keySet()) {
            results.add(userKey);
        }
        return results;
    }
}
