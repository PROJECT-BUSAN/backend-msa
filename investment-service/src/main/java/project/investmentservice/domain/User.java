package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@RequiredArgsConstructor
public class User implements Serializable {

    // User 준비 상태
    public enum ReadyType {
        READY, CANCEL;
    }
    private ReadyType readyType;
    private Long seedMoney;
    private Map<Long, UsersStock> companies = new HashMap();

    public User(Long seedMoney) {
        this.readyType = ReadyType.CANCEL;
        this.seedMoney = seedMoney;
    }
}
