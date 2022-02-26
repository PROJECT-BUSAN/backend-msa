package project.investmentservice.domain;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Test
    public void createUser() {
        User user = new User(1000L);
        assertEquals(user.getReadyType(), User.ReadyType.CANCEL);
        assertEquals(user.getSeedMoney(), 1000L);
    }

}
