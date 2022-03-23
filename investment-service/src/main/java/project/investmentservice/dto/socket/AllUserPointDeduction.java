package project.investmentservice.dto.socket;

import lombok.Data;

import java.util.List;

@Data
public class AllUserPointDeduction{
    private List<Long> userIds;
    private double fee;

    public AllUserPointDeduction(List<Long> userIds, double fee) {
        this.userIds = userIds;
        this.fee = fee;
    }
}