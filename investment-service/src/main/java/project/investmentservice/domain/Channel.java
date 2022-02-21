package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class Channel {

    private String id;
    private Long channelNum;
    private Long channelName;
    private int LimitOfParticipants;
    private Long entryFee;
    private List<User> users = new ArrayList<>();
    private Long pointPsum;


    public static Channel create(String channelName, Long channelNum, int LimitOfParticipants, Long entryFee, Long hostId) {
        // 채널 정보 생성 + host 추가
        Channel channel = new Channel();
        channel.id = UUID.randomUUID().toString();
        channel.channelNum = channelNum;
        channel.LimitOfParticipants = LimitOfParticipants;
        channel.entryFee = entryFee;
        User user = new User(hostId, entryFee);
        channel.users.add(user);
        channel.pointPsum = entryFee;
        return channel;
    }

    public void addUser(Long userId) {
        User user = new User(userId, entryFee);
        this.users.add(user);
        this.pointPsum += this.entryFee;
    }

    public void removeUser(Long userId) {
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).getId() == userId) {
                users.remove(i);
                this.pointPsum -= this.entryFee;
                break;
            }
        }
    }
}
