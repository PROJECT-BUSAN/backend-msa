package project.investmentservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Company;
import project.investmentservice.service.CompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/investment")
public class CompanyApiController {
    private final CompanyService companyService;

    /**
     * KOSPI, KOSDAQ에 상장된 기업 리스트를 모두 조회한다. 
     */
    @GetMapping("/company")
    public List<CompanyResult> CompanyAllV1() {
        List<Company> companyList = companyService.findAll();
        List<CompanyResult> result = companyList.stream()
                .map(c -> new CompanyResult(c))
                .collect(Collectors.toList());

        return result;
    }
    

    @Data
    @AllArgsConstructor
    static class CompanyResult {
        private String stock_name;
        private String stock_code;

        public CompanyResult(Company company) {
            this.stock_name = company.getStock_name();
            this.stock_code = company.getStock_code();
        }
    }

    @Data
    @AllArgsConstructor
    static class TestResult {
        private String message;
        private String data;
    }
}

