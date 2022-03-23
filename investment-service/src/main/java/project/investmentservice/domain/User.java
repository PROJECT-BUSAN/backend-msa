package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import project.investmentservice.enums.UserReadyType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserReadyType readyType;
    private double seedMoney;
    private String name;
    private Map<Long, UsersStock> companies;

    public User(double seedMoney, String name) {
        this.readyType = UserReadyType.CANCEL;
        this.seedMoney = seedMoney;
        this.companies = new HashMap();
        this.name = name;
    }

    /**
     * 현재 유저의 수익률을 반환한다.
     */
//    public double getStockYield() {
//        return (seedMoney - initialSeedMoney) * 100.0 / initialSeedMoney;
//    }
    
    
    public void addCompany(Long companyId) {
        UsersStock usersStock = new UsersStock(0.0, 0.0, 0.0);
        this.companies.put(companyId, usersStock);
    }
}
