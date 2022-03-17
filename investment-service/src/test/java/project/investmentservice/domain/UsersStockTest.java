//package project.investmentservice.domain;
//
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UsersStockTest {
//
//    @Test
//    public void renewalStock() {
//        //given
//        UsersStock usersStock = new UsersStock(0.0, 12L, 0.0);
//        double averagePrice = 213.0;
//        Long quantity = 12L;
//        double totalPrice = 32313.0;
//
//        //when
//        usersStock.renewalStock(averagePrice, quantity, totalPrice);
//
//        //then
//        Assertions.assertEquals(usersStock.getAveragePrice(), averagePrice);
//        Assertions.assertEquals(usersStock.getQuantity(), quantity);
//        Assertions.assertEquals(usersStock.getTotalPrice(), totalPrice);
//
//    }
//}
