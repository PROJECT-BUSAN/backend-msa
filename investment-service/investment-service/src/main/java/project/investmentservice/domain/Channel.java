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
    private String id;
    private Long channelNum;
    private int numOfParticipants;
    private Long entryFee;
    private List<Long> users = new ArrayList<>();
    private List<Boolean> readyState = new ArrayList<>();
    private List<Long> seedMoney = new ArrayList<>();
    private Long pointPsum;

    public Channel(Long channelNum, int numOfParticipants, Long entryFee, Long managerId) {
        this.channelNum = channelNum;
        this.numOfParticipants = numOfParticipants;
        this.entryFee = entryFee;
        this.users.add(managerId);
        this.readyState.add(false);
        this.seedMoney.add(entryFee);
        this.pointPsum = entryFee;
    }

    public boolean addUser(Long userId) {
        this.users.add(userId);
        this.readyState.add(false);
        this.seedMoney.add(this.entryFee);
        this.pointPsum += this.entryFee;
        return true;
    }

    public void removeUser(Long userId) {
        for(int i=0; i<users.size(); i++) {
            if(users.get(i) == userId) {
                users.remove(i);
                readyState.remove(i);
                seedMoney.remove(i);
                this.pointPsum -= this.entryFee;
                break;
            }
        }
    }
}
