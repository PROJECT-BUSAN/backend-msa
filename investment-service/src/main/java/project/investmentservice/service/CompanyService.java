package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Company;
import project.investmentservice.repository.CompanyRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public HashSet<Long> selectInGameCompany() {
        List<Company> all = companyRepository.findAll();
        int companySize = all.size();
        
        HashSet<Long> companyIds = new HashSet<>();

        while(companyIds.size() < 6){
            Long newId = (long) (Math.random() * companySize) + 1L;
            companyIds.add((long)(Math.random() * companySize) + 1);
        }

        return companyIds;
    }

}
