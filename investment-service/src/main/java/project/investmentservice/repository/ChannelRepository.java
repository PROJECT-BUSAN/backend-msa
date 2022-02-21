package project.investmentservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;
import project.investmentservice.domain.Channel;
import project.investmentservice.pubsub.RedisSubscriber;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChannelRepository {

    //채널(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    //구독 처리 서비스
    private final RedisSubscriber redisSubscriber;

    //Redis 설정
    private static final String CHANNEL = "CHANNEL";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Channel> opsHashChannel;

    //채널에 메시지를 발행하기 위한 redis topic 정보. 서버별로 채널에 매치되는 topic 정보를 Map에 넣어 channelId로 찾을 수있게 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChannel = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<Channel> findAllChannel() {
        return opsHashChannel.values(CHANNEL);
    }

    public Channel findChannelById(String id) {
        return opsHashChannel.get(CHANNEL, id);
    }

    /**
     * 채널 생성 : 서버간 채널 공유를 위해 redis hash에 채널 저장
     */
    public Channel createChannel(Channel channel) {
        opsHashChannel.put(CHANNEL, channel.getId(), channel);
        return channel;
    }

    /**
     * 채널 삭제 : redis hash에서 채널 삭제
     * host만 삭제가능
     */
    public void deleteChannel(Channel channel) {
        ChannelTopic topic = topics.get(channel.getId());
        redisMessageListenerContainer.removeMessageListener(redisSubscriber, topic);
        opsHashChannel.delete(CHANNEL, channel.getId());
        topics.remove(channel.getId());
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정.
     */
    public void enterChannel(String channelId) {
        ChannelTopic topic = topics.get(channelId);
        if(topic == null) {
            topic = new ChannelTopic(channelId);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(channelId, topic);
        }
    }

    /**
     * 채팅방 퇴장 : topic에서 Listener 제거
     */
    public void exitChannel(String channelId, Long userId) {
        ChannelTopic topic = topics.get(channelId);
        System.out.println("topic = " + topic);
    }

    public ChannelTopic getTopic(String channelId) {
        return topics.get(channelId);
    }
}
