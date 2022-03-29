package project.investmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import project.investmentservice.domain.Company;

@Getter
public class CompanyDto {

    @Data
    @AllArgsConstructor
    public static class CompanyResult {
        private String stock_name;
        private String stock_code;

        public CompanyResult(Company company) {
            this.stock_name = company.getStock_name();
            this.stock_code = company.getStock_code();
        }
    }
}
