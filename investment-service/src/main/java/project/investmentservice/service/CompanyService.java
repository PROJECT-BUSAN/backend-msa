package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Company;
import project.investmentservice.repository.CompanyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}
