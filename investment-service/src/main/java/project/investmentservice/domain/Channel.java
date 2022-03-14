package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter @Setter
@RequiredArgsConstructor
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private Long channelNum;
    private String channelName;
    private int LimitOfParticipants;
    private double entryFee;
    private Map<Long, User> users = new HashMap<>();
    private double pointPsum;
    private Long hostId;


    public static Channel create(String channelName, Long channelNum, int LimitOfParticipants, double entryFee, Long hostId) {
        // 채널 정보 생성 + host 추가
        Channel channel = new Channel();
        channel.id = UUID.randomUUID().toString();
        channel.channelName = channelName;
        channel.channelNum = channelNum;
        channel.LimitOfParticipants = LimitOfParticipants;
        channel.entryFee = entryFee;
        channel.hostId = hostId;

        User user = new User(entryFee);
        user.setReadyType(User.ReadyType.READY);
        channel.users.put(hostId, user);
        channel.pointPsum = entryFee;
        return channel;
    }

}
