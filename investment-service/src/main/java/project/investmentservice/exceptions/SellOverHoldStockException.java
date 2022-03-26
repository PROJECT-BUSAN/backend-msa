package project.investmentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 보유 수량보다 판매하려는 주식의 수가 많은 경우
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class SellOverHoldStockException extends RuntimeException {
    public SellOverHoldStockException(String message) {
        super(message);
    }
}
