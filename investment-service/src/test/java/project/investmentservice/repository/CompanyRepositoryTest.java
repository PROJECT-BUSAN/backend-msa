package project.investmentservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.investmentservice.domain.Company;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyRepositoryTest {
    
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void findAllTest() {
        // given
        Company company1 = new Company();
        company1.setStock_code("111");
        company1.setStock_name("A");
        companyRepository.save(company1);

        Company company2 = new Company();
        company2.setStock_code("112");
        company2.setStock_name("B");
        companyRepository.save(company2);
        // when
        List<Company> companyList = companyRepository.findAll();
        
        // then
        assertEquals(companyList.size(), 2);
    }

    @Test
    public void findOneTest() {
        // given
        // beforeEach

        // when
        Company company1 = companyRepository.findOne(1L);
        Company company2 = companyRepository.findOne(2L);
        
        // then
        assertEquals(company1.getId(), 1L);
        assertEquals(company2.getId(), 2L);

    }
}
