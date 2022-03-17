//package project.investmentservice.service;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import project.investmentservice.domain.Company;
//import project.investmentservice.repository.CompanyRepository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class CompanyServiceTest {
//
//    @Autowired
//    private CompanyService companyService;
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Test
//    public void findAll() {
//        //given
//        Company company1 = new Company();
//        company1.setStock_code("111");
//        company1.setStock_name("A");
//        companyRepository.save(company1);
//
//        Company company2 = new Company();
//        company2.setStock_code("112");
//        company2.setStock_name("B");
//        companyRepository.save(company2);
//
//        //when
//        List<Company> companyList = companyService.findAll();
//
//        //then
//        assertEquals(companyList.size(), 2);
//    }
//
//    @Test
//    public void findCompany() {
//        //given
//        Company company = new Company();
//        company.setStock_code("111");
//        company.setStock_name("A");
//        companyRepository.save(company);
//
//        //when
//        Company findCompany = companyService.findOneCompany(company.getId());
//
//        //then
//        assertEquals(company.getId(), findCompany.getId());
//        assertEquals(company.getStock_name(), findCompany.getStock_name());
//        assertEquals(company.getStock_code(), findCompany.getStock_code());
//    }
//
//    @Test
//    public void selectInGameCompany() {
//        //given
//
//        //when
//
//        //then
//    }
//
//}
