package project.investmentservice.dto.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.investmentservice.enums.HttpReturnType;

@Data
@AllArgsConstructor
public class SellStockResponse {
    private HttpReturnType type;
    private double averagePrice;
    private Long quantity;
    private double seedMoney;
}
