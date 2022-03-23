package project.investmentservice.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.investmentservice.domain.Company;

@Data
@AllArgsConstructor
public class CompanyResult {
    private String stock_name;
    private String stock_code;

    public CompanyResult(Company company) {
        this.stock_name = company.getStock_name();
        this.stock_code = company.getStock_code();
    }
}