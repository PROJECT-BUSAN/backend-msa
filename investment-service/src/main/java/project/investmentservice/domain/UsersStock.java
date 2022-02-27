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
    private double totalPrice;

    public void renewalStock(double averagePrice, Long quantity, double totalPrice) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public UsersStock(double averagePrice, Long quantity, double totalPrice) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
