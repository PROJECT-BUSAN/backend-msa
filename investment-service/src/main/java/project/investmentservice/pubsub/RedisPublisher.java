package project.investmentservice.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Tttt;
import project.investmentservice.domain.dto.ServerMessage;
import project.investmentservice.domain.dto.StockGameEndMessage;
import project.investmentservice.domain.dto.StockInfoMessage;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ServerMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

    public void publishStock(ChannelTopic topic, StockInfoMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

    public void publishEndMessage(ChannelTopic topic, StockGameEndMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

    public void test(ChannelTopic topic, Tttt t) {
        System.out.println("topic = " + topic.getTopic());
        redisTemplate.convertAndSend(topic.getTopic(), t);

    }
}