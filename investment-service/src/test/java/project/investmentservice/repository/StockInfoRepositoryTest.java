//package project.investmentservice.repository;
//
//
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.investmentservice.domain.Company;
//import project.investmentservice.domain.StockInfo;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class StockInfoRepositoryTest {
//    
//    @Autowired
//    private StockInfoRepository stockInfoRepository;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//    
//
//    @Test
//    @Transactional
//    public void findStockTest() {
//        // given
//        Company company1 = new Company();
//        company1.setStock_code("111");
//        company1.setStock_name("A");
//
//        LocalDate d1 = LocalDate.of(2022, 2, 1);
//        LocalDate d2 = LocalDate.of(2022, 2, 2);
//        LocalDate d3 = LocalDate.of(2022, 2, 3);
//
//        StockInfo stockInfoObject1 = createStockInfoObject(d1, 1111.11, 2222.22, 3333.33, 4444.44, 123123, company1);
//        StockInfo stockInfoObject2 = createStockInfoObject(d2, 1111.11, 2222.22, 3333.33, 4444.44, 123123, company1);
//        StockInfo stockInfoObject3 = createStockInfoObject(d3, 1111.11, 2222.22, 3333.33, 4444.44, 123123, company1);
//        
//        companyRepository.save(company1);
//        stockInfoRepository.save(stockInfoObject1);
//        stockInfoRepository.save(stockInfoObject2);
//        stockInfoRepository.save(stockInfoObject3);
//
//        Company company = companyRepository.findOne(company1.getId());
//
//        // when
//        List<StockInfo> stockInfoList = stockInfoRepository.findStock(company.getId());
//        
//        // then
//        assertEquals(stockInfoList.size(), 3);
//        
//    }
//
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
//}
