package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.investmentservice.enums.SocketServerMessageType;

import java.time.LocalDate;

/**
 * 10초에 한 번씩 주가 정보 전송에 쓰이는 객체
 */
@Getter
public class StockInfoMessage extends PublishMessage{

    private LocalDate date;
    private double close;
    private double open;
    private double high;
    private double low;
    private int volume;
    private Long company_id;

    public StockInfoMessage(SocketServerMessageType type, LocalDate date, double close, double open, double high, double low, int volume, Long company_id) {
        super(type);
        this.date = date;
        this.close = close;
        this.open = open;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.company_id = company_id;
    }
}
