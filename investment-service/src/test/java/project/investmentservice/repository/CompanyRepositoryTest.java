package project.investmentservice.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.investmentservice.domain.Company;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyRepositoryTest {
    
    @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void createCompany() {
        Company company1 = new Company();
        company1.setStock_code("111");
        company1.setStock_name("A");
        companyRepository.save(company1);

        Company company2 = new Company();
        company2.setStock_code("112");
        company2.setStock_name("B");
        companyRepository.save(company2);

        System.out.println("company2 = " + company2.getId());
        System.out.println("company1 = " + company1.getId());

    }

    @Test
    public void findAllTest() {
        // given @Before

        // when
        List<Company> companyList = companyRepository.findAll();
        
        // then
        assertEquals(companyList.size(), 4);
        System.out.println("companyList = " + companyList.get(0).getStock_name());
        System.out.println("companyList = " + companyList.get(1).getStock_name());

    }

    @Test
    public void findOneTest() {
        // given @Before

        // when
        Company company1 = companyRepository.findOne(1L);
        Company company2 = companyRepository.findOne(2L);
        
        // then
        assertEquals(company1.getId(), 1L);
        assertEquals(company2.getId(), 2L);

    }
}
