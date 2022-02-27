package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class User implements Serializable {

    // User 준비 상태
    public enum ReadyType {
        READY, CANCEL;
    }
    private ReadyType readyType;
    private Long initialSeedMoney;
    private Long seedMoney;
    private Map<Long, UsersStock> companies = new HashMap();

    public User(Long seedMoney) {
        this.readyType = ReadyType.CANCEL;
        this.seedMoney = seedMoney;
    }

    /**
     * 현재 유저의 수익률을 반환한다.
     */
    public double getStockYield() {
        return (seedMoney - initialSeedMoney) * 100.0 / initialSeedMoney;
    }
}
