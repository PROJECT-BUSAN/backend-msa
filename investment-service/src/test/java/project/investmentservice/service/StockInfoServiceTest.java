//package project.investmentservice.service;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import project.investmentservice.domain.Company;
//import project.investmentservice.domain.StockInfo;
//
//import java.time.LocalDate;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class StockInfoServiceTest {
//
//    @Autowired
//    private StockInfoService stockInfoService;
//
//    @Test
//    public void getPeriodStockInfo() {
//        //given
//
//
//        //when
//
//        //then
//
//    }
//
//    private StockInfo createStockInfoObject(LocalDate date, double close, double open, double high, double low, int volume, Company company) {
//        StockInfo stockInfo = new StockInfo();
//        stockInfo.setDate(date);
//        stockInfo.setHigh(high);
//        stockInfo.setLow(low);
//        stockInfo.setOpen(open);
//        stockInfo.setClose(close);
//        stockInfo.setVolume(volume);
//        stockInfo.setCompany(company);
//
//        company.getStockInfos().add(stockInfo);
//
//        return stockInfo;
//    }
//
//}
