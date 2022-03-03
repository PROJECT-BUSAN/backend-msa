package project.investmentservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.InvestmentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


import static project.investmentservice.api.InvestmentApiController.PurchaseStockResponse.returnType.SUCCESS;
import static project.investmentservice.api.InvestmentApiController.SellStockResponse.returnType;

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
        
        if(!flag) return new PurchaseStockResponse(PurchaseStockResponse.returnType.FAIL, 0, 0L, 0L);
        else {
            Channel channel = channelService.findOneChannel(channelId);
            User user = channel.getUsers().get(request.getUserId());
            UsersStock usersStock = user.getCompanies().get(request.getCompanyId());
            
            double averagePrice = usersStock.getAveragePrice();
            Long quantity = usersStock.getQuantity();
            double seedMoney = user.getSeedMoney();

            return new PurchaseStockResponse(SUCCESS, averagePrice, quantity, seedMoney);
        }
    }

    // 주식 판매
    @PostMapping("/sell/{channelId}")
    public SellStockResponse sellStock(@PathVariable("channelId") String channelId, @RequestBody @Valid StockRequest request) {
        boolean flag = investmentService.sellStock(channelId, request);
        if(flag) {
            Channel channel = channelService.findOneChannel(channelId);
            return new SellStockResponse(returnType.SUCCESS, channel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getAveragePrice(), channel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getQuantity(), channel.getUsers().get(request.getUserId()).getSeedMoney());
        }
        else {
            return new SellStockResponse(returnType.FAIL, 0.0, 0L, 0.0);
        }
    }

    @Data
    public static class StockRequest {
        @NotNull
        private Long userId;
        @NotNull
        private Long companyId;
        @NotNull
        private double price;
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
        public enum returnType {
            SUCCESS, FAIL
        }
        private returnType type;
        private double averagePrice;
        private Long quantity;
        private double seedMoney;
    }

}
