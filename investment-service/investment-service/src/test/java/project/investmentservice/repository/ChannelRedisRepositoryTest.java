package project.investmentservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.investmentservice.domain.Channel;
import project.investmentservice.service.ChannelService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelRedisRepositoryTest {

    @Autowired
    private ChannelRedisRepository channelRedisRepository;


    @Test
    public void channelRedisRepository_Test() {
        //given
        Channel channel = new Channel();

        channel.getUsers().add(1L);
        channel.getUsers().add(2L);
        channel.setPointPsum(3000L);

        //when
        channelRedisRepository.save(channel);
        Channel findChannel = channelRedisRepository.findById(channel.getId()).get();
        List<Long> usersId = findChannel.getUsers();

        //then
        Assertions.assertEquals(usersId.size(), 2);

    }
}
