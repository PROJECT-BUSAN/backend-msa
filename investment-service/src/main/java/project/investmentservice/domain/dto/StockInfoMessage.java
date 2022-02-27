package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StockInfoMessage {
    private LocalDate date;
    private float close;
    private float open;
    private float high;
    private float low;
    private int volume;
    private Long company_id;
}
