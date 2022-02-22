package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter @Setter
@RequiredArgsConstructor
public class Channel {

    private String id;
    private Long channelNum;
    private Long channelName;
    private int LimitOfParticipants;
    private Long entryFee;
    private Map<Long, User> users = new HashMap<>();
    private Long pointPsum;
    private Long hostId;


    public static Channel create(String channelName, Long channelNum, int LimitOfParticipants, Long entryFee, Long hostId) {
        // 채널 정보 생성 + host 추가
        Channel channel = new Channel();
        channel.id = UUID.randomUUID().toString();
        channel.channelNum = channelNum;
        channel.LimitOfParticipants = LimitOfParticipants;
        channel.entryFee = entryFee;
        User user = new User(entryFee);
        channel.users.put(hostId, user);
        channel.pointPsum = entryFee;
        return channel;
    }

    public User addUser(Long userId) {
        User user = new User(entryFee);
        this.users.put(userId, user);
        this.pointPsum += this.entryFee;
        return user;
    }

    public void removeUser(Long userId) {
        users.remove(userId);
    }
}
