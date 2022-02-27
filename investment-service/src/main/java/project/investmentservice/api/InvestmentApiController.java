package project.investmentservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.domain.dto.ServerMessage;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.InvestmentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Map;

import static project.investmentservice.api.InvestmentApiController.PurchaseStockResponse.returnType.FAIL;
import static project.investmentservice.api.InvestmentApiController.PurchaseStockResponse.returnType.SUCCESS;
import static project.investmentservice.domain.dto.ClientMessage.MessageType.ENTER;
import static project.investmentservice.domain.dto.ServerMessage.MessageType.RENEWAL;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/investment")
public class InvestmentApiController {

    @Autowired
    private final InvestmentService investmentService;
    @Autowired
    private final ChannelService channelService;

    // 주식 구매
    @PostMapping("/purchase/{channelId}")
    public PurchaseStockResponse purchaseStock(@PathVariable("channelId") String channelId, @RequestBody @Valid StockRequest request) {
        boolean flag = investmentService.purchaseStock(channelId, request);
        
        if(!flag) return new PurchaseStockResponse(FAIL, 0, 0L, 0L);
        else {
            Channel channel = channelService.findOneChannel(channelId);
            User user = channel.getUsers().get(request.getUserId());
            UsersStock usersStock = user.getCompanies().get(request.getCompanyId());
            
            double averagePrice = usersStock.getAveragePrice();
            Long quantity = usersStock.getQuantity();
            Long seedMoney = user.getSeedMoney();

            return new PurchaseStockResponse(SUCCESS, averagePrice, quantity, seedMoney);
        }
    }

    // 주식 판매
    @GetMapping("/sell/{channelId}")
    public void sellStock(@PathVariable("channelId") String channelId, @RequestBody @Valid StockRequest request) {

    }

    @Data
    public static class StockRequest {
        @NotNull
        private Long userId;
        @NotNull
        private Long companyId;
        @NotNull
        private Long price;
        @NotNull
        private Long quantity;
    }

    @Data
    @AllArgsConstructor
    public static class PurchaseStockResponse {

        public enum returnType {
            SUCCESS, FAIL
        }

        private returnType type;
        private double averagePrice;
        private Long quantity;
        private double seedMoney;
    }

    @Data
    @AllArgsConstructor
    public static class SellStockResponse {
        private double averagePrice;
        private Long quantity;
        private double seedMoney;
    }

}
