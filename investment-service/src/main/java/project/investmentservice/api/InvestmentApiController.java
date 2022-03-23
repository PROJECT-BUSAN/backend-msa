package project.investmentservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.dto.investment.PurchaseStockResponse;
import project.investmentservice.dto.investment.SellStockResponse;
import project.investmentservice.dto.investment.StockRequest;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.InvestmentService;
import project.investmentservice.utils.HttpApiController;

import javax.validation.Valid;

import static project.investmentservice.enums.HttpReturnType.FAIL;
import static project.investmentservice.enums.HttpReturnType.SUCCESS;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/investment")
public class InvestmentApiController {

    private final InvestmentService investmentService;
    private final ChannelService channelService;
    private final HttpApiController httpApiController;

    /**
     * 주식 구매 API
     * 
     * @param channelId
     * @param request
     * @return
     */
    @PostMapping("/purchase/{channelId}")
    public PurchaseStockResponse purchaseStockV1(@PathVariable("channelId") String channelId, @RequestBody @Valid StockRequest request) {
        boolean flag = investmentService.purchaseStock(channelId, request);
        
        if(!flag) return new PurchaseStockResponse(FAIL, 0, 0L, 0L);
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
            User user = channel.getUsers().get(request.getUserId());
            UsersStock usersStock = user.getCompanies().get(request.getCompanyId());
            return new SellStockResponse(SUCCESS, usersStock.getAveragePrice(), usersStock.getQuantity(), user.getSeedMoney());
        }
        else {
            return new SellStockResponse(FAIL, 0.0, 0L, 0.0);
        }
    }
}
