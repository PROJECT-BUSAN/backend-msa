package project.investmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import project.investmentservice.enums.HttpReturnType;

import javax.validation.constraints.NotNull;

@Getter
public class InvestmentDto {

    @Data
    public static class StockRequest {
        @NotNull
        private Long userId;
        @NotNull
        private Long companyId;
        @NotNull
        private double price;
        @NotNull
        private Long quantity;
    }

    @Data
    @AllArgsConstructor
    public static class SellStockResponse {
        private HttpReturnType type;
        private double averagePrice;
        private Long quantity;
        private double seedMoney;
    }

    @Data
    @AllArgsConstructor
    public static class PurchaseStockResponse {
        private HttpReturnType type;
        private double averagePrice;
        private Long quantity;
        private double seedMoney;
    }

}
