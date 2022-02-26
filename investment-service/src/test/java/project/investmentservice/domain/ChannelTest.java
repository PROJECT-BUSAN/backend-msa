package project.investmentservice.domain;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelTest {

    @Test
    public void createChannel() {
        //given
        Channel channel = Channel.create("helloworld!", 2L, 10, 1000L, 123L);

        //when

        //then
        assertEquals(channel.getUsers().size(), 1);
        assertEquals(channel.getChannelName(), "helloworld!");
    }

}
