package project.investmentservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.dto.InvestmentDto.StockRequest;
import project.investmentservice.enums.HttpReturnType;
import project.investmentservice.enums.TradeType;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.InvestmentService;
import project.investmentservice.utils.HttpApiController;

import javax.validation.Valid;

import static project.investmentservice.dto.InvestmentDto.*;
import static project.investmentservice.enums.TradeType.FAIL;
import static project.investmentservice.enums.TradeType.SUCCESS;


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
    public ResponseEntity<TradeResponse> purchaseStockV1(@PathVariable("channelId") String channelId, @RequestBody @Valid TradeRequest request) {
        Long userId = request.getUserId();
        boolean flag = investmentService.purchaseStock(channelId, request);
        User user = channelService.findUserById(channelId, userId);
        if(!flag) {
            TradeResponse response = new TradeResponse(FAIL, "보유 금액보다 주문량이 많습니다.", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            TradeResponse response = new TradeResponse(SUCCESS, "주문에 성공하였습니다.", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // 주식 판매
    @PostMapping("/sell/{channelId}")
    public ResponseEntity<TradeResponse> sellStock(@PathVariable("channelId") String channelId, @RequestBody @Valid TradeRequest request) {
        Long userId = request.getUserId();
        Long companyId = request.getCompanyId();
        boolean flag = investmentService.sellStock(channelId, request);
        User user = channelService.findUserById(channelId, userId);

        if(!flag) {
            TradeResponse response = new TradeResponse(FAIL, "보유량보다 판매량이 많습니다.", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            TradeResponse response = new TradeResponse(SUCCESS, "판매에 성공했습니다.", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
