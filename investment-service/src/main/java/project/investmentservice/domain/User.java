package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {
    private Long id;
    private Boolean ready;
    private Long seedMoney;

    public User(Long id, Boolean ready, Long seedMoney) {
        this.id = id;
        this.ready = ready;
        this.seedMoney = seedMoney;
    }
}
