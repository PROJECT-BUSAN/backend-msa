package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class StockResult {
    private Long companyId;
    private String stockName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String message;
}
