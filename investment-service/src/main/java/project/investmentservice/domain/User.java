package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class User {

    // User 준비 상태
    public enum ReadyType {
        READY, CANCEL;
    }
    private ReadyType readyType;
    private Long seedMoney;


    public User(Long seedMoney) {
        this.readyType = ReadyType.CANCEL;
        this.seedMoney = seedMoney;
    }
}
