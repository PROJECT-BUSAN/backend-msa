package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.StockInfo;
import project.investmentservice.repository.StockInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    
    private final StockInfoRepository stockInfoRepository;

    public List<StockInfo> getPeriodStockInfo(Long company_id) {
        List<StockInfo> stockInfoList = stockInfoRepository.findStock(company_id);
        int startIdx = (int)Math.random() * (stockInfoList.size() - 60);
        return stockInfoList.subList(startIdx, startIdx + 60);
    }
    
}
