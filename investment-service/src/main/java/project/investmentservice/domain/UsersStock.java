package project.investmentservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsersStock implements Serializable {

    private double averagePrice;
    private Long quantity;
    private Long prefixSum;

    public void renewalStock(double averagePrice, Long quantity, Long prefixSum) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.prefixSum = prefixSum;
    }

    public UsersStock(double averagePrice, Long quantity, Long prefixSum) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.prefixSum = prefixSum;
    }
}
