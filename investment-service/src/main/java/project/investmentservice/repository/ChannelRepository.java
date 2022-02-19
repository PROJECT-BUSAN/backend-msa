//package project.investmentservice.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Repository;
//import project.investmentservice.domain.Channel;
//import project.investmentservice.pubsub.RedisSubscriber;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Repository
//public class ChannelRepository {
//
//    //채널(topic)에 발행되는 메시지를 처리할 Listner
//    private final RedisMessageListenerContainer redisMessageListenerContainer;
//
//    //구독 처리 서비스
//    private final RedisSubscriber redisSubscriber;
//
//    //Redis 설정
//    private static final String CHANNEL = "CHANNEL";
//    private final RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, Channel> opsHashChannel;
//
//    //채널에 메시지를 발행하기 위한 redis topic 정보. 서버별로 채널에 매치되는 topic 정보를 Map에 넣어 channelId로 찾을 수있게 한다.
//    private Map<String, ChannelTopic> topics;
//
////    @PostConstruct
////    private void init() {
////        opsHashChannel = redisTemplate.opsForHash();
////        topics = new HashMap<>();
////    }
//
//    public List<Channel> findAllChannel() {
//        return opsHashChannel.values(CHANNEL);
//    }
//
//    public Channel findChannelById(String id) {
//        return opsHashChannel.get(CHANNEL, id);
//    }
//
//    /**
//     * 채널 생성 : 서버간 채널 공유를 위해 redis hash에 채널 저장
//     */
//    public Channel createChannel(String channelName, Long channelNum, int LimitOfParticipants, Long entryFee, Long hostId) {
//        Channel channel = Channel.create(channelName, channelNum, LimitOfParticipants, entryFee, hostId);
//        opsHashChannel.put(CHANNEL, channel.getId(), channel);
//        return channel;
//    }
//
//    /**
//     * 채널 입장 : 서버간 채널 공유를 위해 redis hash에 채널 저장
//     */
//    public void enterChannel(String channelId) {
//        ChannelTopic topic = topics.get(channelId);
//        if(topic == null) {
//            topic = new ChannelTopic(channelId);
//            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
//            topics.put(channelId, topic);
//        }
//    }
//
//    public ChannelTopic getTopic(String channelId) {
//        return topics.get(channelId);
//    }
//}
