package project.investmentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResult {
    private String userName;
    private Long userId;
    private double userProfit;
    private double userProfitRate;
}
