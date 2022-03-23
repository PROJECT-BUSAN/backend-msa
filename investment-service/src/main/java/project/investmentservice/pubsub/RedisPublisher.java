package project.investmentservice.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.TestDomain;
import project.investmentservice.dto.SocketDto;

import static project.investmentservice.dto.SocketDto.*;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    
    public void publish(ChannelTopic topic, PublishMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

    public void test(ChannelTopic topic, TestDomain t) {
        System.out.println("topic = " + topic.getTopic());
        redisTemplate.convertAndSend(topic.getTopic(), t);
    }
}