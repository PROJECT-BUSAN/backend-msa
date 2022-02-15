package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
@RedisHash(value = "channelId", timeToLive = 1000)
public class Channel {

    @Id @GeneratedValue
    private Long id;
    private Long channelNum;
    private int numOfParticipants;
    private Long entryFee;
    private List<User> users = new ArrayList<>();
    private Long pointPsum;


    public Channel(Long channelNum, int numOfParticipants, Long entryFee, Long managerId) {
        this.channelNum = channelNum;
        this.numOfParticipants = numOfParticipants;
        this.entryFee = entryFee;
        User user = new User(managerId, true, entryFee);
        this.users.add(user);
        this.pointPsum = entryFee;
    }

    public void addUser(Long userId) {
        User user = new User(userId, false, entryFee);
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
