package project.investmentservice.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.investmentservice.api.InvestmentApiController;
import project.investmentservice.api.InvestmentApiController.StockRequest;
import project.investmentservice.domain.Channel;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private ChannelService channelService;


    @Test
    public void purchaseStock() {
        //given
        Channel channel1 = channelService.createChannel("createChannel", 10, 10L, 2L);
        channelService.enterChannel(channel1.getId(), 31L);
        StockRequest request = new StockRequest();
        request.setUserId(31L);
        request.setCompanyId(213L);
        request.setPrice(10L);
        request.setQuantity(10L);

        //when
        boolean flag = investmentService.purchaseStock(channel1.getId(), request);
        Channel findChannel = channelService.findOneChannel(channel1.getId());


        //then
        assertEquals(flag, true);
        assertEquals(findChannel.getUsers().get(31L).getCompanies().get(request.getCompanyId()).getAveragePrice(), 10.0);
        assertEquals(findChannel.getUsers().get(31L).getCompanies().get(request.getCompanyId()).getTotalPrice(), 100L);
        assertEquals(findChannel.getUsers().get(31L).getCompanies().get(request.getCompanyId()).getQuantity(), 10L);
    }

    @Test
    public void sellStock() {
        //given
        Channel channel = channelService.createChannel("createChannel", 10, 10L, 2L);
        channelService.enterChannel(channel.getId(), 31L);
        StockRequest request = new StockRequest();
        request.setUserId(31L);
        request.setCompanyId(213L);
        request.setPrice(10L);
        request.setQuantity(10L);

        boolean flag = investmentService.purchaseStock(channel.getId(), request);

        StockRequest sellRequest = new StockRequest();
        sellRequest.setUserId(31L);
        sellRequest.setPrice(20L);
        sellRequest.setQuantity(8L);
        sellRequest.setCompanyId(213L);

        //when
        boolean flag2 = investmentService.sellStock(channel.getId(), sellRequest);
        Channel findChannel = channelService.findOneChannel(channel.getId());

        //then
        assertEquals(flag, true);
        assertEquals(flag2, true);

        //1000원 시작 -> 100원어치 주식 구매 -> 160원 어치 수익발생 == seedMoney는 1060
        assertEquals(findChannel.getUsers().get(31L).getSeedMoney(), 1060.0);
    }
}
