package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {

    // User 준비 상태
    public enum ReadyType {
        READY, CANCEL;
    }
    private Long id;
    private ReadyType readyType;
    private Long seedMoney;


    public User(Long id, Long seedMoney) {
        this.id = id;
        this.readyType = ReadyType.CANCEL;
        this.seedMoney = seedMoney;
    }
}
