package project.investmentservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Company;
import project.investmentservice.dto.CompanyDto.CompanyResult;
import project.investmentservice.service.CompanyService;

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
    public static class TestResult {
        private String message;
        private String data;
    }
}

