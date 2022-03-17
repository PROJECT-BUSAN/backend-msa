//package project.investmentservice.domain.dto;
//
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import project.investmentservice.domain.User;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static project.investmentservice.domain.dto.ServerMessage.MessageType.NOTICE;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ServerMessageTest {
//
//    @Test
//    public void createServerMessage() {
//        //given
//        User user = new User(1000L);
//        Map<Long, User> users = new HashMap<>();
//        users.put(1L, user);
//
//        //when
//        ServerMessage serverMessage = new ServerMessage(NOTICE, "test", users, "insert");
//
//        //then
//        assertEquals(serverMessage.getType(), NOTICE);
//        assertEquals(serverMessage.getChannelId(), "test");
//        assertEquals(serverMessage.getUsers().get(1L).getReadyType(), User.ReadyType.CANCEL);
//        assertEquals(serverMessage.getMessage(), "insert");
//    }
//}
