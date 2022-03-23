package project.investmentservice.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 유저간의 게임 결과 반환
 * 유저의 이율 기준 내림차순
 */
@Getter
@AllArgsConstructor
public class GameResult {
    private String userName;
    private Long userId;
    private double userProfit;
    private double userProfitRate;
}
