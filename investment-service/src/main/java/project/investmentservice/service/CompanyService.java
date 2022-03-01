package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Company;
import project.investmentservice.domain.StockInfo;
import project.investmentservice.repository.CompanyRepository;
import project.investmentservice.repository.StockInfoRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    
    private final CompanyRepository companyRepository;
    private final StockInfoRepository stockInfoRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    /**
     * 게임 시작 시 주가를 가져올 기업을 선정한다.
     * 1. 기업 선정은 완전히 랜덤이다.
     * 2. 2618개의 기업 중 주가가 60일 이상 있는 기업만 가져온다.
     * @return 선정된 기업의 ID를 set으로 반환한다.
     */
    public HashSet<Long> selectInGameCompany(int num) {
        List<Company> all = companyRepository.findAll();
        int companySize = all.size();
        
        HashSet<Long> companyIds = new HashSet<>();

        while(companyIds.size() < num){
            Long comId = (long) (Math.random() * companySize) + 1L;
            if(stockInfoRepository.findStock(comId).size() < 60){
                continue;
            }
            companyIds.add((long)(Math.random() * companySize) + 1);
        }

        return companyIds;
    }

    public Company findCompany(Long company_id) {
        return companyRepository.findOne(company_id);
    }

}
