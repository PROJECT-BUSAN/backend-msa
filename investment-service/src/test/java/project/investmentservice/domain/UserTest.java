//package project.investmentservice.domain;
//
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserTest {
//
//    @Test
//    public void createUser() {
//        User user = new User(1000.0);
//        assertEquals(user.getReadyType(), User.ReadyType.CANCEL);
//        assertEquals(user.getSeedMoney(), 1000.0);
//    }
//
//    @Test
//    public void addCompany() {
//        //given
//        User user = new User(1000.0);
//
//        //when
//        user.addCompany(31L);
//
//        //then
//        assertEquals(user.getCompanies().get(31L).getAveragePrice(), 0);
//        assertEquals(user.getCompanies().get(31L).getQuantity(), 0L);
//        assertEquals(user.getCompanies().get(31L).getTotalPrice(), 0.0);
//
//    }
//}
