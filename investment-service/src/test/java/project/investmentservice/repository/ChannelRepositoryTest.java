package project.investmentservice.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.investmentservice.domain.Channel;

import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelRepositoryTest implements Serializable {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void createChannel() {
        //given
        Channel channel = Channel.create("첫번째 방", 2L, 10, 10000L, 1234L);

        //when
        Channel saveChannel = channelRepository.createChannel(channel);

        //then
        assertEquals(channel.getId(), saveChannel.getId());
        assertEquals(channel.getChannelNum(), saveChannel.getChannelNum());
        assertEquals(channel.getChannelName(), saveChannel.getChannelName());
        assertEquals(channel.getLimitOfParticipants(), saveChannel.getLimitOfParticipants());
        assertEquals(channel.getEntryFee(), saveChannel.getEntryFee());
        assertEquals(channel.getUsers(), saveChannel.getUsers());
        assertEquals(channel.getPointPsum(), saveChannel.getPointPsum());
        assertEquals(channel.getHostId(), saveChannel.getHostId());
        assertEquals(channel, saveChannel);
    }

    @Test
    public void deleteChannel() {
        //given
        Channel channel = Channel.create("30번째 방", 10L, 10, 10000L, 1234L);
        int beforeChannelCount = channelRepository.findAllChannel().size();
        channelRepository.createChannel(channel);

        //when
        channelRepository.deleteChannel(channel);
        int afterChannelCount = channelRepository.findAllChannel().size();

        //then
        assertEquals(beforeChannelCount, afterChannelCount);
    }

    @Test
    public void findAllChannel() {
        //given

        //when
        List<Channel> channels = channelRepository.findAllChannel();

        //then
        System.out.println("findAllChannel() = " + channels.size());
    }

    @Test
    public void findChannelById() {
        //given
        Channel channel = Channel.create("findChannelById", 11L, 10, 10000L, 1234L);
        channelRepository.createChannel(channel);

        //when
        Channel findChannel = channelRepository.findChannelById(channel.getId());

        //then
        assertEquals(channel.getId(), findChannel.getId());
    }

    @Test
    public void updateChannel() {
        //given
        Channel channel = Channel.create("updateChannel", 11L, 10, 10000L, 1234L);
        channelRepository.createChannel(channel);
        channel.setChannelName("updateChannel2");

        //when
        channelRepository.updateChannel(channel);
        Channel findChannel = channelRepository.findChannelById(channel.getId());

        //then
        assertEquals(findChannel.getChannelName(), "updateChannel2");
        assertEquals(findChannel.getId(), channel.getId());
    }
}
