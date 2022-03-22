package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 게임 진행에 사용된 기업의 이름, 시작날짜, 종료 날짜를 반환
 */
@Getter
@AllArgsConstructor
public class StockResult {
    private Long companyId;
    private String stockName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String message;
}
