//package project.investmentservice.service;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import project.investmentservice.domain.Channel;
//import project.investmentservice.domain.User;
//import static project.investmentservice.enums.UserReadyType.CANCEL;
//import static project.investmentservice.enums.UserReadyType.READY;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ChannelServiceTest {
//
//    @Autowired
//    private ChannelService channelService;
//
//    @Test
//    public void createChannel() {
//        //given
//        int beforeChannelCount = channelService.findAllChannel().size();
//
//        //when
//        channelService.createChannel("createChannel", 10, 10000.0, 2L);
//        int afterChannelCount = channelService.findAllChannel().size();
//
//        //then
//        assertEquals(beforeChannelCount+1, afterChannelCount);
//    }
//
//    @Test
//    public void deleteChannel() {
//        //given
//        int beforeChannelCount = channelService.findAllChannel().size();
//        Channel channel = channelService.createChannel("deleteChannel", 10, 10000.0, 2L);
//
//        //when
//        channelService.deleteChannel(channel);
//        int afterChannelCount = channelService.findAllChannel().size();
//
//        //then
//        assertEquals(beforeChannelCount, afterChannelCount);
//    }
//
//    @Test
//    public void findOneChannel() {
//        //given
//        Channel channel = channelService.createChannel("findOneChannel", 10, 10000L, 2L);
//
//        //when
//        Channel findChannel = channelService.findOneChannel(channel.getId());
//
//        //then
//        assertEquals(channel.getId(), findChannel.getId());
//
//    }
//
//    @Test
//    public void findAllChannel() {
//        //given
//        int beforeChannelCount = channelService.findAllChannel().size();
//        channelService.createChannel("createChannel", 10, 10000.0, 2L);
//
//
//        //when
//        List<Channel> channels = channelService.findAllChannel();
//        int afterChannelCount = channels.size();
//
//        //then
//        assertEquals(beforeChannelCount+1, afterChannelCount);
//    }
//
//    @Test
//    public void enterChannel() {
//        //given
//        Channel channel1 = channelService.createChannel("enterChannel", 10, 10000.0, 2L);
//        Channel channel2 = channelService.createChannel("enterChannel", 10, 900.0, 2L);
//
//        //when
//        int flag1 = channelService.enterChannel(channel1.getId(), 1L);
//        int flag2 = channelService.enterChannel(channel2.getId(), 3L);
//        Channel findChannel = channelService.findOneChannel(channel2.getId());
//
//        //then
//        assertEquals(flag1, 1);
//        assertEquals(flag2, 0);
//        assertEquals(findChannel.getUsers().size(), 2);
//
//    }
//
//    @Test
//    public void exitChannel() {
//        //given
//        Channel channel = channelService.createChannel("exitChannel", 10, 900L, 2L);
//        channelService.enterChannel(channel.getId(), 1L);
//        int beforeUserCount = channelService.findOneChannel(channel.getId()).getUsers().size();
//
//        //when
//        channelService.exitChannel(channel.getId(), 1L);
//        int afterUserCount = channelService.findOneChannel(channel.getId()).getUsers().size();
//
//        //then
//        assertEquals(beforeUserCount, 2);
//        assertEquals(afterUserCount, 1);
//    }
//
//    @Test
//    public void setReady() {
//        //given
//        Channel channel = channelService.createChannel("setReady", 10, 900L, 2L);
//        channelService.enterChannel(channel.getId(), 3L);
//        channelService.enterChannel(channel.getId(), 4L);
//
//        //when
//        channelService.setReady(channel.getId(), 3L);
//        channelService.setReady(channel.getId(), 4L);
//        Map<Long, User> users = channelService.findOneChannel(channel.getId()).getUsers();
//        int readyCount = 0;
//        for (Long key : users.keySet()) {
//            if(users.get(key).getReadyType() == READY){
//                readyCount++;
//            }
//        }
//
//        //then
//        assertEquals(readyCount, 3);
//    }
//
//    @Test
//    public void cancelReady() {
//        //given
//        Channel channel = channelService.createChannel("cancelReady", 10, 900L, 1L);
//        channelService.enterChannel(channel.getId(), 2L);
//        channelService.enterChannel(channel.getId(), 3L);
//        channelService.setReady(channel.getId(), 2L);
//        channelService.setReady(channel.getId(), 3L);
//
//        //when
//        channelService.cancelReady(channel.getId(), 2L);
//        Map<Long, User> users = channelService.findOneChannel(channel.getId()).getUsers();
//
//        //then
//        assertEquals(users.get(1L).getReadyType(), READY);
//        assertEquals(users.get(2L).getReadyType(), CANCEL);
//        assertEquals(users.get(3L).getReadyType(), READY);
//    }
//
//    @Test
//    public void checkReadyState() {
//        //given
//        // 채널1은 전부 ready한 상태, 채널2는 일부가 ready하지 않은 상태를 가정
//
//        Channel channel1 = channelService.createChannel("checkReadyState", 10, 900L, 1L);
//        channelService.enterChannel(channel1.getId(), 2L);
//        channelService.enterChannel(channel1.getId(), 3L);
//        channelService.setReady(channel1.getId(), 2L);
//        channelService.setReady(channel1.getId(), 3L);
//
//        Channel channel2 = channelService.createChannel("checkReadyState", 10, 900L, 1L);
//        channelService.enterChannel(channel2.getId(), 2L);
//        channelService.enterChannel(channel2.getId(), 3L);
//        channelService.setReady(channel2.getId(), 2L);
//
//        //when
//        boolean flag1 = channelService.checkReadyState(channel1.getId());
//        boolean flag2 = channelService.checkReadyState(channel2.getId());
//
//        //then
//        assertEquals(flag1, true);
//        assertEquals(flag2, false);
//    }
//}
