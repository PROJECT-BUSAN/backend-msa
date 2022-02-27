package project.investmentservice.domain.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StockInfoMessage {

    private LocalDate date;
    private double close;
    private double open;
    private double high;
    private double low;
    private int volume;
    private Long company_id;

    public StockInfoMessage(LocalDate date, double close, double open, double high, double low, int volume, Long company_id) {
        this.date = date;
        this.close = close;
        this.open = open;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.company_id = company_id;
    }
}
