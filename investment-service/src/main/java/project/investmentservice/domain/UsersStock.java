package project.investmentservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsersStock implements Serializable {

    private static final long serialVersionUID = 1L;

    private double averagePrice;
    private double quantity;
    private double totalPrice;

    public void renewalStock(double averagePrice, double quantity, double totalPrice) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public UsersStock(double averagePrice, double quantity, double totalPrice) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
