package project.investmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.investmentservice.domain.User;
import project.investmentservice.enums.HttpReturnType;
import project.investmentservice.enums.TradeType;

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

    @Getter
    public static class TradeRequest {
        @NotNull
        private Long userId;
        @NotNull
        private Long companyId;
        @NotNull
        private double quantity;
    }

    @Getter
    @AllArgsConstructor
    public static class TradeResponse {
        private TradeType type;
        private String message;
        private User user;
    }
}
