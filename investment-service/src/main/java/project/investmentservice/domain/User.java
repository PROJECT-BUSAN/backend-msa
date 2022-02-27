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
    private double seedMoney;
    private Map<Long, UsersStock> companies;

    public User(double seedMoney) {
        this.readyType = ReadyType.CANCEL;
        this.seedMoney = seedMoney;
        this.companies = new HashMap();
    }

    public void addCompany(Long companyId) {
        UsersStock usersStock = new UsersStock(0, 0L, 0.0);
        this.companies.put(companyId, usersStock);
    }
}
