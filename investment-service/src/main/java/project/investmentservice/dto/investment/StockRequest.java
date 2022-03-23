package project.investmentservice.dto.investment;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StockRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long companyId;
    @NotNull
    private double price;
    @NotNull
    private Long quantity;
}
