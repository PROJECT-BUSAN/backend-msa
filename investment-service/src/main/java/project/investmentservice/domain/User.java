package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@RequiredArgsConstructor
public class User implements Serializable {

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
