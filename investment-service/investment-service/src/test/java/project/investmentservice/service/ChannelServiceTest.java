package project.investmentservice.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.investmentservice.domain.Channel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Test
    public void createChannel_Test() {
        //given
        Channel channel = channelService.createChannel(1L, 10, 1000L);

        //when

        //then
        assertEquals(channel.getUsers().size(), 1);
        assertEquals(channel.getPointPsum(), 1000L);
    }

    @Test
    public void findOneChannel_Test() {
        //given
        Channel channel = channelService.createChannel(1L, 10, 1000L);

        //when
        Channel findChannel = channelService.findOneChannel(channel.getId());

        //then
        assertEquals(channel.getChannelNum(), findChannel.getChannelNum());
    }

    @Test
    public void findAllChannel_Test() {
        //given
        Iterable<Channel> channels = channelService.findAllChannel();
        
        //when
        
        //then
        for (Channel channel : channels) {
            if(channel == null) continue;
            Channel findChannel = channelService.findOneChannel(channel.getId());
            System.out.println("channel = " + findChannel.getChannelNum());
            System.out.println("findChannel = " + findChannel.getEntryFee());
        }
    }

    @Test
    public void participantChannel_Test() {
        //given
        Channel channel = channelService.createChannel(1L, 10, 1000L);

        //when
        channelService.participantChannel(channel.getId(), 2L, 2000L);
        Channel findChannel = channelService.findOneChannel(channel.getId());

        //then
        assertEquals(findChannel.getUsers().size(), 2);
    }

    @Test
    public void exitChannel_Test() {
        //given
        Channel channel = channelService.createChannel(1L, 10, 1000L);
        channelService.participantChannel(channel.getId(), 2L, 2000L);
        channelService.participantChannel(channel.getId(), 3L, 2000L);

        //when
        channelService.exitChannel(channel.getId(), 2L);
        Channel findChannel = channelService.findOneChannel(channel.getId());
        List<Long> users = findChannel.getUsers();

        //then
        assertEquals(findChannel.getUsers().size(), 2);
        for (Long user : users) {
            System.out.println("user = " + user);
        }
    }
}
